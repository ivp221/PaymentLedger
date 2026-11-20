-- Phase 0 baseline. Tables arrive in Phase 1 (V2__ledger_core.sql).
-- Shared helper used by the append-only triggers on postings and journals.


CREATE FUNCTION forbid_mutation() RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'table % is append-only', TG_TABLE_NAME;
END
$$ LANGUAGE plpgsql;


