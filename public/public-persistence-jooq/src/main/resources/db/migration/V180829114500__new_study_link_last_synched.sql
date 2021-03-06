/*
 * Introduce a new timestamp 'last_synched' for the synchronization to track if an item has been touched.
 * Thus we can assume untouched items do not exist anymore in the core database and can delete them in public.
 */
 
ALTER TABLE new_study_page_link ADD COLUMN IF NOT EXISTS last_synched TIMESTAMP NOT NULL DEFAULT current_timestamp;