import { supabase } from './supabaseClient';
import { toast } from 'react-hot-toast';

// --- CLIENTS API ---
const toDatabaseFormat = (clientData) => {
  return {
    full_name: clientData.fullName,
    phone_number: clientData.phoneNumber,
    profile_picture_url: clientData.profilePictureUrl,
    internal_notes: clientData.internalNotes,
    email: clientData.email,
    address: clientData.address,
  };
};

export const getClients = async (params) => {
  let query = supabase
    .from('clients')
    .select('*, tags(name)', { count: 'exact' });

  // 1. Filtrado por Tags
  if (params.tag && params.tag.length > 0) {
    const { data: clientIds, error: rpcError } = await supabase.rpc(
      'find_clients_with_all_tags',
      {
        tag_names: params.tag,
      }
    );

    if (rpcError) throw rpcError;
    const ids = clientIds.map((c) => c.client_id);
    query = query.in('id', ids);
  }
  // 2. Búsqueda por Texto
  else if (params.q) {
    query = query.or(`full_name.ilike.%${params.q}%,email.ilike.%${params.q}%`);
  }

  // 3. Paginación y Ordenación
  const [sortField, sortDir] = (params.sort || 'fullName,asc').split(',');
  const from = params.page * params.size;
  const to = from + params.size - 1;
  query = query
    .order(sortField, { ascending: sortDir === 'asc' })
    .range(from, to);

  const { data, error, count } = await query;
  if (error) throw error;

  return {
    content: data.map((client) => ({
      id: client.id,
      email: client.email,
      address: client.address,
      isEnabled: client.isEnabled,
      registrationDate: client.created_at,
      fullName: client.full_name,
      phoneNumber: client.phone_number,
      profilePictureUrl: client.profile_picture_url,
      internalNotes: client.internal_notes,
      tags: client.tags ? client.tags.map((t) => t.name) : [],
    })),
    totalElements: count,
    totalPages: Math.ceil((count || 0) / params.size),
  };
};

export const getClientById = async (id) => {
  const { data, error } = await supabase
    .from('clients')
    .select('*, tags(name)')
    .eq('id', id)
    .single();

  if (error) throw error;
  if (!data) throw new Error('Client not found');

  return {
    id: data.id,
    email: data.email,
    address: data.address,
    isEnabled: data.isEnabled,
    registrationDate: data.created_at,
    fullName: data.full_name,
    phoneNumber: data.phone_number,
    profilePictureUrl: data.profile_picture_url,
    internalNotes: data.internal_notes,
    tags: data.tags ? data.tags.map((t) => t.name) : [],
  };
};

export const createClient = async (clientData) => {
  const { tags, ...clientInfo } = clientData;

  const { data: newClient, error: clientError } = await supabase
    .from('clients')
    .insert(toDatabaseFormat(clientInfo))
    .select()
    .single();

  if (clientError) throw clientError;

  if (tags && tags.length > 0) {
    const { data: tagObjects, error: tagsError } = await supabase
      .from('tags')
      .upsert(
        tags.map((name) => ({ name })),
        { onConflict: 'name' }
      )
      .select();

    if (tagsError) throw tagsError;

    const relations = tagObjects.map((tagObj) => ({
      client_id: newClient.id,
      tag_id: tagObj.id,
    }));
    const { error: relationError } = await supabase
      .from('client_tags')
      .insert(relations);
    if (relationError) throw relationError;
  }

  toast.success('Client created successfully!');

  return newClient;
};

export const updateClient = async (id, clientData) => {
  const { tags, ...clientInfo } = clientData;

  const { data: updatedClient, error: clientError } = await supabase
    .from('clients')
    .update(toDatabaseFormat(clientInfo))
    .eq('id', id)
    .select()
    .single();

  if (clientError) throw clientError;

  const { error: deleteError } = await supabase
    .from('client_tags')
    .delete()
    .eq('client_id', id);
  if (deleteError) throw deleteError;

  if (tags && tags.length > 0) {
    const { data: tagObjects, error: tagsError } = await supabase
      .from('tags')
      .upsert(
        tags.map((name) => ({ name })),
        { onConflict: 'name' }
      )
      .select();

    if (tagsError) throw tagsError;

    const relations = tagObjects.map((tagObj) => ({
      client_id: updatedClient.id,
      tag_id: tagObj.id,
    }));
    const { error: relationError } = await supabase
      .from('client_tags')
      .insert(relations);
  }
  toast.success('Client updated successfully!');

  return updatedClient;
};

export const deleteClient = async (id) => {
  const { error } = await supabase.from('clients').delete().eq('id', id);
  if (error) throw error;
  toast.success('Client deleted successfully!');
};

// --- TAGS API ---

export const getAllTags = async () => {
  const { data, error } = await supabase.from('tags').select('name');
  if (error) throw error;
  return data.map((tag) => tag.name);
};
