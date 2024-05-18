CREATE OR REPLACE FUNCTION createTable()
RETURNS VOID AS $$
BEGIN

END;
$$
LANGUAGE plpgsql;

-- Drop 


CREATE OR REPLACE FUNCTION dropTable()
RETURNS VOID AS $$
BEGIN

END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION truncate_all() RETURNS VOID AS
$$
DECLARE
    table_name TEXT;
BEGIN
    FOR table_name IN
        SELECT info.table_name
        FROM information_schema.tables info
        WHERE table_schema = 'public' AND table_type = 'BASE TABLE' AND info.table_name != 'users'
    LOOP
        EXECUTE 'TRUNCATE TABLE ' || 'public' || '.' || table_name || ' CASCADE';
    END LOOP;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION drop_all() RETURNS VOID AS
$$
DECLARE
    table_name TEXT;
BEGIN
    FOR table_name IN
        SELECT info.table_name
        FROM information_schema.tables info
        WHERE table_schema = 'public' AND table_type = 'BASE TABLE' AND info.table_name != 'users'
    LOOP
        EXECUTE 'DROP TABLE ' || 'public' || '.' || table_name || ' CASCADE';
    END LOOP;
END;
$$
LANGUAGE plpgsql;

