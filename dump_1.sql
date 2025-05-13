--
-- PostgreSQL database cluster dump
--

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Drop databases (except postgres and template1)
--

DROP DATABASE "ConversayDB";




--
-- Drop roles
--

DROP ROLE postgres;


--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:6vLvICpRtXgCwKIeV9QfEQ==$XuD8kVLn8NgnWnvvc+Z6t0xJCDxQJmcsfLu7YaXeGQg=:llH//OMVLWgbUsUa3s+34Xcez6g8+xPbKRh3rHq6HuU=';

--
-- User Configurations
--








--
-- Databases
--

--
-- Database "template1" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.6 (Debian 15.6-1.pgdg120+2)
-- Dumped by pg_dump version 15.6 (Debian 15.6-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

UPDATE pg_catalog.pg_database SET datistemplate = false WHERE datname = 'template1';
DROP DATABASE template1;
--
-- Name: template1; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE template1 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE template1 OWNER TO postgres;

\connect template1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE template1; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE template1 IS 'default template for new databases';


--
-- Name: template1; Type: DATABASE PROPERTIES; Schema: -; Owner: postgres
--

ALTER DATABASE template1 IS_TEMPLATE = true;


\connect template1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE template1; Type: ACL; Schema: -; Owner: postgres
--

REVOKE CONNECT,TEMPORARY ON DATABASE template1 FROM PUBLIC;
GRANT CONNECT ON DATABASE template1 TO PUBLIC;


--
-- PostgreSQL database dump complete
--

--
-- Database "ConversayDB" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.6 (Debian 15.6-1.pgdg120+2)
-- Dumped by pg_dump version 15.6 (Debian 15.6-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ConversayDB; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "ConversayDB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE "ConversayDB" OWNER TO postgres;

\connect "ConversayDB"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: convy; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA convy;


ALTER SCHEMA convy OWNER TO postgres;

--
-- Name: chat_role; Type: TYPE; Schema: convy; Owner: postgres
--

CREATE TYPE convy.chat_role AS ENUM (
    'Participant',
    'Admin',
    'Moderator'
);


ALTER TYPE convy.chat_role OWNER TO postgres;

--
-- Name: chat_type; Type: TYPE; Schema: convy; Owner: postgres
--

CREATE TYPE convy.chat_type AS ENUM (
    'direct',
    'conversation'
);


ALTER TYPE convy.chat_type OWNER TO postgres;

--
-- Name: gender; Type: TYPE; Schema: convy; Owner: postgres
--

CREATE TYPE convy.gender AS ENUM (
    'male',
    'female'
);


ALTER TYPE convy.gender OWNER TO postgres;

--
-- Name: role_action; Type: TYPE; Schema: convy; Owner: postgres
--

CREATE TYPE convy.role_action AS ENUM (
    'invite',
    'mute',
    'move_between_channels',
    'temporary_disable'
);


ALTER TYPE convy.role_action OWNER TO postgres;

--
-- Name: server_chanel_type; Type: TYPE; Schema: convy; Owner: postgres
--

CREATE TYPE convy.server_chanel_type AS ENUM (
    'chat',
    'voice'
);


ALTER TYPE convy.server_chanel_type OWNER TO postgres;

--
-- Name: CAST (character varying AS convy.gender); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS convy.gender) WITH INOUT AS IMPLICIT;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: app_role; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.app_role (
    app_role_id bigint NOT NULL,
    role_name character varying(30) NOT NULL
);


ALTER TABLE convy.app_role OWNER TO postgres;

--
-- Name: app_role_app_role_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.app_role_app_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.app_role_app_role_id_seq OWNER TO postgres;

--
-- Name: app_role_app_role_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.app_role_app_role_id_seq OWNED BY convy.app_role.app_role_id;


--
-- Name: chat; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.chat (
    chat_id bigint NOT NULL,
    picture_id bigint,
    chat_name character varying(40),
    chat_type character varying
);


ALTER TABLE convy.chat OWNER TO postgres;

--
-- Name: chat_chat_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.chat_chat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.chat_chat_id_seq OWNER TO postgres;

--
-- Name: chat_chat_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.chat_chat_id_seq OWNED BY convy.chat.chat_id;


--
-- Name: chat_user; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.chat_user (
    chat_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE convy.chat_user OWNER TO postgres;

--
-- Name: chat_user_role; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.chat_user_role (
    user_id integer NOT NULL,
    chat_id integer NOT NULL,
    chat_role character varying NOT NULL
);


ALTER TABLE convy.chat_user_role OWNER TO postgres;

--
-- Name: file; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.file (
    file_id bigint NOT NULL,
    filename text NOT NULL,
    file_size integer NOT NULL,
    mime_type text NOT NULL
);


ALTER TABLE convy.file OWNER TO postgres;

--
-- Name: file_file_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.file_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.file_file_id_seq OWNER TO postgres;

--
-- Name: file_file_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.file_file_id_seq OWNED BY convy.file.file_id;


--
-- Name: friends; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.friends (
    my_id bigint NOT NULL,
    friend_id bigint NOT NULL,
    confirmed_date date
);


ALTER TABLE convy.friends OWNER TO postgres;

--
-- Name: message; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.message (
    message_id bigint NOT NULL,
    chat_id bigint,
    sender_id bigint,
    send_timestamp timestamp without time zone NOT NULL,
    replying_message_id bigint,
    message_content text
);


ALTER TABLE convy.message OWNER TO postgres;

--
-- Name: message_file; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.message_file (
    message_id bigint NOT NULL,
    file_id bigint NOT NULL
);


ALTER TABLE convy.message_file OWNER TO postgres;

--
-- Name: message_message_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.message_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.message_message_id_seq OWNER TO postgres;

--
-- Name: message_message_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.message_message_id_seq OWNED BY convy.message.message_id;


--
-- Name: message_seen; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.message_seen (
    message_id bigint NOT NULL,
    user_id bigint NOT NULL,
    seen_timestamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE convy.message_seen OWNER TO postgres;

--
-- Name: refresh_token; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.refresh_token (
    token_id bigint NOT NULL,
    refresh_token character varying(500) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE convy.refresh_token OWNER TO postgres;

--
-- Name: refresh_token_token_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.refresh_token_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.refresh_token_token_id_seq OWNER TO postgres;

--
-- Name: refresh_token_token_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.refresh_token_token_id_seq OWNED BY convy.refresh_token.token_id;


--
-- Name: refresh_token_user_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.refresh_token_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.refresh_token_user_id_seq OWNER TO postgres;

--
-- Name: refresh_token_user_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.refresh_token_user_id_seq OWNED BY convy.refresh_token.user_id;


--
-- Name: server; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server (
    server_id character varying(255) NOT NULL,
    picture_id bigint,
    servername character varying(40) NOT NULL
);


ALTER TABLE convy.server OWNER TO postgres;

--
-- Name: server_channel; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server_channel (
    channel_id character varying(255) NOT NULL,
    channel_name character varying(40) NOT NULL,
    server_id character varying(255) NOT NULL,
    chat_id bigint,
    channel_type character varying(40) NOT NULL
);


ALTER TABLE convy.server_channel OWNER TO postgres;

--
-- Name: server_channel_channel_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_channel_channel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_channel_channel_id_seq OWNER TO postgres;

--
-- Name: server_channel_channel_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_channel_channel_id_seq OWNED BY convy.server_channel.channel_id;


--
-- Name: server_channel_chat_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_channel_chat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_channel_chat_id_seq OWNER TO postgres;

--
-- Name: server_channel_chat_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_channel_chat_id_seq OWNED BY convy.server_channel.chat_id;


--
-- Name: server_channel_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_channel_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_channel_server_id_seq OWNER TO postgres;

--
-- Name: server_channel_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_channel_server_id_seq OWNED BY convy.server_channel.server_id;


--
-- Name: server_role; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server_role (
    server_id character varying(255) NOT NULL,
    role_name character varying(40) NOT NULL
);


ALTER TABLE convy.server_role OWNER TO postgres;

--
-- Name: server_role_action; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server_role_action (
    server_id bigint NOT NULL,
    role_name character varying(40) NOT NULL,
    role_action character varying(40) NOT NULL
);


ALTER TABLE convy.server_role_action OWNER TO postgres;

--
-- Name: server_role_action_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_role_action_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_role_action_server_id_seq OWNER TO postgres;

--
-- Name: server_role_action_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_role_action_server_id_seq OWNED BY convy.server_role_action.server_id;


--
-- Name: server_role_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_role_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_role_server_id_seq OWNER TO postgres;

--
-- Name: server_role_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_role_server_id_seq OWNED BY convy.server_role.server_id;


--
-- Name: server_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_server_id_seq OWNER TO postgres;

--
-- Name: server_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_server_id_seq OWNED BY convy.server.server_id;


--
-- Name: server_user; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server_user (
    server_id character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    join_timestamp timestamp without time zone DEFAULT now() NOT NULL,
    nick character varying(40)
);


ALTER TABLE convy.server_user OWNER TO postgres;

--
-- Name: server_user_new_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_user_new_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_user_new_server_id_seq OWNER TO postgres;

--
-- Name: server_user_new_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_user_new_server_id_seq OWNED BY convy.server_user.server_id;


--
-- Name: server_user_new_user_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_user_new_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_user_new_user_id_seq OWNER TO postgres;

--
-- Name: server_user_new_user_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_user_new_user_id_seq OWNED BY convy.server_user.user_id;


--
-- Name: server_user_role; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.server_user_role (
    server_id character varying(255) NOT NULL,
    role_name character varying(40) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE convy.server_user_role OWNER TO postgres;

--
-- Name: server_user_role_server_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_user_role_server_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_user_role_server_id_seq OWNER TO postgres;

--
-- Name: server_user_role_server_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_user_role_server_id_seq OWNED BY convy.server_user_role.server_id;


--
-- Name: server_user_role_user_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.server_user_role_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.server_user_role_user_id_seq OWNER TO postgres;

--
-- Name: server_user_role_user_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.server_user_role_user_id_seq OWNED BY convy.server_user_role.user_id;


--
-- Name: user; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy."user" (
    user_id bigint NOT NULL,
    username character varying(35),
    picture_id bigint,
    email character varying(35),
    passwd character varying(75),
    user_gender convy.gender NOT NULL
);


ALTER TABLE convy."user" OWNER TO postgres;

--
-- Name: user_app_role; Type: TABLE; Schema: convy; Owner: postgres
--

CREATE TABLE convy.user_app_role (
    user_id bigint NOT NULL,
    app_role_id bigint NOT NULL
);


ALTER TABLE convy.user_app_role OWNER TO postgres;

--
-- Name: user_user_id_seq; Type: SEQUENCE; Schema: convy; Owner: postgres
--

CREATE SEQUENCE convy.user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE convy.user_user_id_seq OWNER TO postgres;

--
-- Name: user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: convy; Owner: postgres
--

ALTER SEQUENCE convy.user_user_id_seq OWNED BY convy."user".user_id;


--
-- Name: app_role app_role_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.app_role ALTER COLUMN app_role_id SET DEFAULT nextval('convy.app_role_app_role_id_seq'::regclass);


--
-- Name: chat chat_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat ALTER COLUMN chat_id SET DEFAULT nextval('convy.chat_chat_id_seq'::regclass);


--
-- Name: file file_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.file ALTER COLUMN file_id SET DEFAULT nextval('convy.file_file_id_seq'::regclass);


--
-- Name: message message_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message ALTER COLUMN message_id SET DEFAULT nextval('convy.message_message_id_seq'::regclass);


--
-- Name: refresh_token token_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.refresh_token ALTER COLUMN token_id SET DEFAULT nextval('convy.refresh_token_token_id_seq'::regclass);


--
-- Name: refresh_token user_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.refresh_token ALTER COLUMN user_id SET DEFAULT nextval('convy.refresh_token_user_id_seq'::regclass);


--
-- Name: server server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server ALTER COLUMN server_id SET DEFAULT nextval('convy.server_server_id_seq'::regclass);


--
-- Name: server_channel channel_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel ALTER COLUMN channel_id SET DEFAULT nextval('convy.server_channel_channel_id_seq'::regclass);


--
-- Name: server_channel server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel ALTER COLUMN server_id SET DEFAULT nextval('convy.server_channel_server_id_seq'::regclass);


--
-- Name: server_channel chat_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel ALTER COLUMN chat_id SET DEFAULT nextval('convy.server_channel_chat_id_seq'::regclass);


--
-- Name: server_role server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_role ALTER COLUMN server_id SET DEFAULT nextval('convy.server_role_server_id_seq'::regclass);


--
-- Name: server_role_action server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_role_action ALTER COLUMN server_id SET DEFAULT nextval('convy.server_role_action_server_id_seq'::regclass);


--
-- Name: server_user server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user ALTER COLUMN server_id SET DEFAULT nextval('convy.server_user_new_server_id_seq'::regclass);


--
-- Name: server_user user_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user ALTER COLUMN user_id SET DEFAULT nextval('convy.server_user_new_user_id_seq'::regclass);


--
-- Name: server_user_role server_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user_role ALTER COLUMN server_id SET DEFAULT nextval('convy.server_user_role_server_id_seq'::regclass);


--
-- Name: server_user_role user_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user_role ALTER COLUMN user_id SET DEFAULT nextval('convy.server_user_role_user_id_seq'::regclass);


--
-- Name: user user_id; Type: DEFAULT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy."user" ALTER COLUMN user_id SET DEFAULT nextval('convy.user_user_id_seq'::regclass);


--
-- Data for Name: app_role; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.app_role (app_role_id, role_name) FROM stdin;
1	Admin
2	Moderator
\.


--
-- Data for Name: chat; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.chat (chat_id, picture_id, chat_name, chat_type) FROM stdin;
1	\N	\N	direct
2	\N	\N	direct
29	\N	\N	direct
30	\N	\N	direct
32	\N	\N	conversation
33	\N	\N	conversation
34	\N	\N	conversation
35	\N	\N	conversation
36	\N	\N	conversation
37	\N	\N	conversation
\.


--
-- Data for Name: chat_user; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.chat_user (chat_id, user_id) FROM stdin;
1	1
1	2
2	1
2	3
29	1
29	5
30	1
30	55
\.


--
-- Data for Name: chat_user_role; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.chat_user_role (user_id, chat_id, chat_role) FROM stdin;
1	1	Admin
1	2	Admin
1	29	Admin
1	30	Admin
\.


--
-- Data for Name: file; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.file (file_id, filename, file_size, mime_type) FROM stdin;
1	altairka.jpg	201567	image/jpeg
2	apxbl3.jpg	201567	image/jpeg
3	james.jpg	201567	image/jpeg
4	mr.bitches.jpg	201567	image/jpeg
5	dr.draw.jpg	201567	image/jpeg
6	g.party.jpg	201567	image/jpeg
7	midjourney.jpg	201567	image/jpeg
8	enclave.jpg	201567	image/jpeg
9	infiniteRealm.jpg	201567	image/jpeg
10	file_acc739f0-0d9c-4717-a69c-72c33747375ffda503ea-9919-4a65-abc4-43d2dd0663e3.png	175947	image/png
11	file_379c70db-6e3b-4c34-9d30-ebab9946e8ef61d6a33f-0ad4-411b-ab83-0be4fc7ef3cd.jpg	72568	image/jpeg
12	file_b5c56c11-e93c-4c00-8969-e46dbacddac96425c3cc-362c-42a3-9c56-7bd299479e43.png	175947	image/png
13	file_564dfacb-eb1b-43ad-a7e5-b57660d16863d509faf6-043c-4480-84f6-eff7ef19c35d.jpg	72568	image/jpeg
14	file_bf5bce98-8586-4669-a224-b558c19be225846cfb67-6878-44d2-8d3a-c345f7b5ae27.jpg	72568	image/jpeg
15	file_16e36703-9c40-44c1-8d2e-0a923442ad97192529f5-3cef-4746-9ee5-ef5883dca2e9.jpg	72568	image/jpeg
\.


--
-- Data for Name: friends; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.friends (my_id, friend_id, confirmed_date) FROM stdin;
1	2	2023-04-01
1	3	2023-04-01
1	5	2023-04-01
2	1	2023-04-01
3	1	2023-04-01
5	1	2023-04-01
2	3	2023-05-01
3	2	2023-05-01
6	1	2023-09-23
1	6	2023-09-23
56	1	2025-03-01
1	56	2025-03-01
57	1	2025-03-01
1	57	2025-03-01
59	1	2025-03-01
1	59	2025-03-01
58	1	2025-03-01
1	58	2025-03-01
60	1	2025-03-01
1	60	2025-03-01
61	1	2025-03-01
1	61	2025-03-01
62	1	2025-03-01
1	62	2025-03-01
63	1	2025-03-01
1	63	2025-03-01
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.message (message_id, chat_id, sender_id, send_timestamp, replying_message_id, message_content) FROM stdin;
1	1	1	2023-05-10 14:39:29.50471	\N	тестовое сообщение от альтаирки шелби
27	1	2	2023-05-21 16:21:31.646534	1	тут затестим reply
28	1	1	2023-05-27 14:30:34.057	\N	привет, как жизнь?
29	1	1	2023-05-27 17:47:41.868	\N	zxc
30	2	1	2023-05-27 18:12:56.137	\N	hello
31	1	2	2023-05-27 21:46:43.251	\N	hey
32	1	1	2023-05-27 21:48:10.201	\N	за окном три дня дождя
33	2	3	2023-05-28 11:04:11.65	\N	го смотреть аниме
34	2	1	2023-05-28 11:05:03.963	\N	а какое?
\.


--
-- Data for Name: message_file; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.message_file (message_id, file_id) FROM stdin;
\.


--
-- Data for Name: message_seen; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.message_seen (message_id, user_id, seen_timestamp) FROM stdin;
1	2	2023-10-21 11:02:06.643291
27	1	2023-10-21 11:02:06.643291
28	2	2023-10-21 11:02:06.643291
29	2	2023-10-22 11:02:06.643291
31	1	2023-10-22 11:02:06.643291
30	3	2023-10-22 11:02:06.643291
33	1	2023-10-23 11:02:06.643291
\.


--
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.refresh_token (token_id, refresh_token, user_id) FROM stdin;
10	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTY5NTY3NTI4MSwiZXhwIjoxNjk2MjAwODgxfQ.NNbkD-SNxw4ouMQ3BhyWqdeqBtpLFTKbObmEhVt1aGg	3
15	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MCIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk1NzE2NzExLCJleHAiOjE2OTYyNDIzMTF9.zoluvQ1R4SKvSqd97yQZUibVg-w5i_tF_TA3lSjcF7I	50
16	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MSIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk1NzE3MzI3LCJleHAiOjE2OTYyNDI5Mjd9.-S8iZLHzbXeHozRr-IjEa31DIqGFX4-8DctS6x1-ZlA	51
17	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MiIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk1Nzk4MTU1LCJleHAiOjE2OTYzMjM3NTV9.aeR8vUequQsQBMlYHAphSDWgo8jvv_jxIdgIyWysxFQ	52
18	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MyIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk1Nzk4NTE3LCJleHAiOjE2OTYzMjQxMTd9.xPwl_wgqN4PjJdF1Mz8QDUK0CC4JzP37g18suYdee48	53
19	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NCIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk1Nzk4NTI4LCJleHAiOjE2OTYzMjQxMjh9.8H-FXJUCZv5PGQUWWN264q_6xs_5Vjwg8GgHxr_zJSo	54
20	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNjk2NjA5OTk3LCJleHAiOjE2OTcxMzU1OTd9.trZkCJIyqOVRCX3iYgCHzFbmXtVDM2VQywmgmcF4r2A	1
21	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNjk2ODc0MDczLCJleHAiOjE2OTczOTk2NzN9.i8kvcl163C5JBYZa7jLHmV201ziLm-zM9HDdyxjmfrw	1
22	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNjk3Mjg5NDIxLCJleHAiOjE3Mjg4MjU0MjF9.OLddfhJA5r6bjaeSxhzN8ymI0DpGHDLM0clkQLyShbo	1
23	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NSIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNjk3MjkwNjMzLCJleHAiOjE3Mjg4MjY2MzN9.xxyyV0GEvyFpkMaOikIQrpRQtk08iDvcEJ8cwpFp4EE	55
24	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNjk3NDYxODY5LCJleHAiOjE3Mjg5OTc4Njl9.RWtV-2lQP09Ej_toiBbzT__ULDwo4aFbnh3LroUh_fU	1
25	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNjk3NjQ1MTkzLCJleHAiOjE3MjkxODExOTN9.BLwzh4gtvK2RY8ip-lAftIiqTxJEV4qAs_fJZPPXUww	1
26	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTY5ODQzNTA4NywiZXhwIjoxNzI5OTcxMDg3fQ.f4Nqhm9UWyn4AxMHPzH1ZsvhJvOSDLjHzjYyYoCK3Xo	3
27	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTcwMDA4MDI1MCwiZXhwIjoxNzMxNjE2MjUwfQ.Dw54XVasrp3blLcguLplnNmamTNf4aTziIuq_n7Lv2c	3
28	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTcwMDA4MDgyMSwiZXhwIjoxNzMxNjE2ODIxfQ.KbbgdCpzFD_UyV6a9nnpp-RiD-e2tFnoUXUZoqO67iE	2
29	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTcwMDMxMjIxOCwiZXhwIjoxNzMxODQ4MjE4fQ.cNzQuD2eE6G6rKFvrG60g6rNmjtkFtYVUXJI-dyB5VM	2
30	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTcwMDY5MTQ4MywiZXhwIjoxNzMyMjI3NDgzfQ.1VQb9RAR6J2QOXyVATZ6g2CiFw_9j5MDDsDlR3OmENQ	2
31	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTcwMDkzNDU5MSwiZXhwIjoxNzMyNDcwNTkxfQ._gt8pu2NfDWF8QJRpOknRyrQXTMx2JJsTyiCXDNxr_o	2
32	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNzA3NzY4NzAyLCJleHAiOjE3MzkzMDQ3MDJ9.DOv2gyT7vzVMVE-sdXHmI8r0Os8Cr9N-8dA6Sizd2Zc	1
33	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNzM3ODk5ODY1LCJleHAiOjE3Njk0MzU4NjV9.XvKow2Vtlu0nnmS29JWUBd5WJ09iFKdjiN4ycCQp4NM	1
34	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNzM3OTAxMDU4LCJleHAiOjE3Njk0MzcwNTh9.blSeD4ikPZ10DF-ketGO347PwkVmq1iq907fu-1U7Bo	1
35	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNzQwNTk3NTA2LCJleHAiOjE3NzIxMzM1MDZ9.xev_muybrh854mVXZGuYVlsw3aRaBH2a6MO4tfYQWgE	1
36	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NiIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwNjAyNjUwLCJleHAiOjE3NzIxMzg2NTB9.EO5-ffrQxscJCDwZW8DKpLPuir2ON_TLb32opqYD268	56
37	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NiIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODI1Njc3LCJleHAiOjE3NzIzNjE2Nzd9.g0SkJsRf4xmbn3hiXh2EnQ6ISRRnaQB-__74oKCuB40	56
38	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1NyIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODM4ODk0LCJleHAiOjE3NzIzNzQ4OTR9.k39kBTksezopXuR_q-t761-jQVNQxk_zmsxJiznnqVU	57
39	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1OCIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODQzMzgxLCJleHAiOjE3NzIzNzkzODF9.Y2LONl3M3PEnBvw0A7aNdU2kdWswrPbwHrwd5CSwBNI	58
40	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1OSIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYwNjc3LCJleHAiOjE3NzIzOTY2Nzd9.7DqUiWuH-Sfa-7zhv85gKAWIn7xUxQpusucPNsB3d08	59
41	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1OCIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYwNzQxLCJleHAiOjE3NzIzOTY3NDF9.-CN7CYuJWiKUeM8GldtXXpsYbLuU_M293-74REXJZ9A	58
42	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MCIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYzMTQwLCJleHAiOjE3NzIzOTkxNDB9.8ZjkP5EBIASRiBtY5qt8vIau89_B8-qq5m9S_rSpf34	60
43	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MSIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYzMjM2LCJleHAiOjE3NzIzOTkyMzZ9.XPS7MId0Uljhl6tupEA37L4S3EltDM6XzZECqEWCo6Y	61
44	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MiIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYzMjg1LCJleHAiOjE3NzIzOTkyODV9.l07Jn1J8YMDYpkiIrHlezARfVR8Ws3pZ7euS5gidQbw	62
45	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MyIsIkFwcFJvbGVzIjpbXSwiaWF0IjoxNzQwODYzMzI5LCJleHAiOjE3NzIzOTkzMjl9.KCaY44A6odGyWeTWGoplRHha2u_S7kagsylIepfy190	63
46	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0MTUzNjQ5NSwiZXhwIjoxNzczMDcyNDk1fQ.GycUiuovP8H0WlbJXoCdUiEi23I1edPzayQ6PlLw8jw	3
47	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0MjAzMzkxNiwiZXhwIjoxNzczNTY5OTE2fQ.oF_Ts02NQ2pFAvu6jg-t2KGe0lSSlCwqk_oYmaAOw_8	3
48	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0MjUwMDQ2MiwiZXhwIjoxNzc0MDM2NDYyfQ.DmC--xRDSLdIqQ-VN7vhMWLpwsKc8YaedmGbYO8-DNI	3
49	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0MjUwMDU3NiwiZXhwIjoxNzc0MDM2NTc2fQ.UN9YiSDzGSBDEL0ti3QY-j5PdmGFdkztWfgdep1Vg7c	3
50	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0Mjg0Nzg4OCwiZXhwIjoxNzc0MzgzODg4fQ.eOkgAMKz1l1eiOhz2IUOVV8G-R3KIshC19JV3T_yHQY	3
51	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiQXBwUm9sZXMiOlsiQWRtaW4iXSwiaWF0IjoxNzQ1NjY0NjY2LCJleHAiOjE3NzcyMDA2NjZ9.AEBDeYjq7alqsyB9HnflNdcPKldCMKME5FP0gE19rjQ	1
52	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0NTY2NTMyMywiZXhwIjoxNzc3MjAxMzIzfQ.8pkOugS0aKK4HhXhfZJMf-L-mA4pdGXDmlh5bVRIoXI	3
53	eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwiQXBwUm9sZXMiOlsiTW9kZXJhdG9yIl0sImlhdCI6MTc0Njc4MjMwNSwiZXhwIjoxNzc4MzE4MzA1fQ.Uu8XuXKvOqYpLxGH3IK8ZOWbW0zJfQzIhV-59hrxrTg	3
\.


--
-- Data for Name: server; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server (server_id, picture_id, servername) FROM stdin;
hello0mg	9	Аниме берлога
sisisigma	8	Майнкрафт
wetruy	7	Сигма бойчик
gay_party	6	Гейская туса
\.


--
-- Data for Name: server_channel; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server_channel (channel_id, channel_name, server_id, chat_id, channel_type) FROM stdin;
zxc1234	ShelbyText1	hello0mg	32	text
zxc2345	Аниме	hello0mg	33	voice
my_pretty_channel	Party SQUADE	gay_party	\N	voice
577f7864-6bdf-4598-b757-1b519bf3b9d3	Для общения	gay_party	34	voice
b24c91a1-2189-4992-bcbe-46fec2405b2d	Аниме	gay_party	35	voice
f613b811-70b0-4f5a-82d6-9943492e54f1	vanilla	sisisigma	36	voice
ef567f11-106d-4f91-a97d-992a9bc9b1e0	Дота Два	gay_party	37	voice
\.


--
-- Data for Name: server_role; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server_role (server_id, role_name) FROM stdin;
\.


--
-- Data for Name: server_role_action; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server_role_action (server_id, role_name, role_action) FROM stdin;
\.


--
-- Data for Name: server_user; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server_user (server_id, user_id, join_timestamp, nick) FROM stdin;
hello0mg	1	2023-04-28 17:39:06.483304	\N
hello0mg	2	2023-04-28 17:39:06.483304	\N
hello0mg	5	2023-04-28 17:39:06.483304	\N
sisisigma	1	2023-04-28 17:38:38.516358	\N
sisisigma	3	2023-04-28 17:38:38.516358	\N
wetruy	1	2023-04-28 17:38:15.202824	\N
wetruy	6	2023-04-28 17:38:15.202824	\N
gay_party	1	2023-04-28 17:37:22.667258	Altairka Shelby
gay_party	2	2023-04-28 17:37:22.667258	Смотрящий
gay_party	3	2023-04-28 17:37:22.667258	Dief
gay_party	5	2023-04-28 17:37:22.667258	Slimak
\.


--
-- Data for Name: server_user_role; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.server_user_role (server_id, role_name, user_id) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy."user" (user_id, username, picture_id, email, passwd, user_gender) FROM stdin;
2	APXbl3	2	JohnMarlow777@mail.ru	$2a$12$1Z5/zIuYsNKCy0FyFtNPBem2sPbvTDCbxjjUAOs7MldrOlcXy0/Me	male
5	Winer	3	james777Shelby@gmail.com	$2a$12$vOrSV9ac8uQ1KkBM4uSTpeQ1yCCGy0AEjKGfetY8sd/175jN2.o32	male
6	Dr.Draw	5	dr_draw999@gmail.com	$2a$12$6DtvpnU7vq5kH1WtRVa7B.AfoSczPqvQo3xHliqk67RE.umifPLX.	male
3	Dief	4	nikita_komkom@mail.ru	$2a$12$JTTHdrn7etnWI/IP3GgKEeZSiALRqON2Uh1r2iuCwqbRR/VPq.Wbi	male
50	Johnny	\N	zxc123@gmail.com	$2a$12$0HNw/vQJAnwhnA1UN4rzVu1Vk8rVU6gh1VxjlUvV.cB.YaLvBgyVq	male
51	Carla	\N	zxc1234@gmail.com	$2a$12$6n9KLoigatt9rjJhUiKWHeuKknTCGnXdRtEExNnLltJ/sq0J.Q1zq	female
52	Carlax	\N	zxc12345@gmail.com	$2a$12$zob3kufd7y3JEp.SIH.fquapAJX9rGinERd.p1GyjQSjtZyfpg58S	female
53	Bell	\N	zxc123456@gmail.com	$2a$12$u2lmXqgo8HlHLks7Kl6Ys.9hXxAhK9MmYblof4clO6JNs/Ko3Zx0C	female
54	Bella	\N	zxc1234567@gmail.com	$2a$12$DUeAwOTWOcdZ7y3CziY/Pu4w9yFKMdB1UH1Mbo7Q5IZe8UivPrHOi	female
55	ivan	\N	zxc2345678@gmail.com	$2a$12$HEfOwMtFWBFph.2Ho0oxQe9qxfPtIFk.P.HlJDPcW8C6y8Jy.Qwki	male
56	Ahahaman	\N	zxc@gmail.com	$2a$12$TRTERutf/4iWxqHgHF3eFeHDAy9fn7k8Z9upv37KfYDzOpXJMQek6	male
1	Altairka	1	zxc666Shelby@gmail.com	$2a$12$uxGQza4UDEQgthf3xC1bh.4NUPEc9lUMQ/Bk0JgJdRD1HOeS4ThRy	male
57	JohnShelby	\N	johnshelby666@gmail.com	$2a$12$XR7gsTLL65fYUpmzm2mx0Oz109T8cgmXIqqdzoyiucjfsJ6KeAADi	male
58	MorningStar	\N	morning@gmail.com	$2a$12$ygRd6P81kDv//15bdydIzOICaXKjx42ImAWSKHu8wAVReiHNzEdGa	male
59	Maximka	\N	mmm@gmail.com	$2a$12$XA9eDVM8irRCpTTaPUq4cO2JsZH1VYZoiGl2gzYiRrGHsvGIedfBm	male
60	Riptip	\N	riptip@gmail.com	$2a$12$a02uFEI2oniJTDSnJhNXU.Jo2zqKJN2Ce4A3T1..jA5iJ.lxqbTgC	female
61	cucumberlo	\N	ahaha@gmail.com	$2a$12$3QLyHcB5IgVUrxWsxVbqmeir0zFwq.wsDSQbE3tbnGY4fOSQ/EM.e	female
62	Jullly	\N	xxx@gmail.com	$2a$12$EN0KNUiWjcZ0ZxUhI7gt0.eDL76/ndin80yoUhuJtM/iCWNYBAINa	female
63	LillyJane	\N	gggg@gmail.com	$2a$12$KRiNKthl69hmQ9D2Wt3dVOyZi3j5oI3glc5YMHS8y9fUbsrJw1BvS	female
\.


--
-- Data for Name: user_app_role; Type: TABLE DATA; Schema: convy; Owner: postgres
--

COPY convy.user_app_role (user_id, app_role_id) FROM stdin;
1	1
2	2
3	2
5	2
6	2
\.


--
-- Name: app_role_app_role_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.app_role_app_role_id_seq', 2, true);


--
-- Name: chat_chat_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.chat_chat_id_seq', 37, true);


--
-- Name: file_file_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.file_file_id_seq', 15, true);


--
-- Name: message_message_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.message_message_id_seq', 193, true);


--
-- Name: refresh_token_token_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.refresh_token_token_id_seq', 53, true);


--
-- Name: refresh_token_user_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.refresh_token_user_id_seq', 1, false);


--
-- Name: server_channel_channel_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_channel_channel_id_seq', 2, true);


--
-- Name: server_channel_chat_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_channel_chat_id_seq', 1, false);


--
-- Name: server_channel_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_channel_server_id_seq', 1, false);


--
-- Name: server_role_action_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_role_action_server_id_seq', 1, false);


--
-- Name: server_role_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_role_server_id_seq', 1, false);


--
-- Name: server_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_server_id_seq', 4, true);


--
-- Name: server_user_new_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_user_new_server_id_seq', 1, false);


--
-- Name: server_user_new_user_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_user_new_user_id_seq', 1, false);


--
-- Name: server_user_role_server_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_user_role_server_id_seq', 1, false);


--
-- Name: server_user_role_user_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.server_user_role_user_id_seq', 1, false);


--
-- Name: user_user_id_seq; Type: SEQUENCE SET; Schema: convy; Owner: postgres
--

SELECT pg_catalog.setval('convy.user_user_id_seq', 63, true);


--
-- Name: app_role app_role_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.app_role
    ADD CONSTRAINT app_role_pkey PRIMARY KEY (app_role_id);


--
-- Name: chat_user chat_Id_user_id_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user
    ADD CONSTRAINT "chat_Id_user_id_pkey" PRIMARY KEY (chat_id, user_id);


--
-- Name: chat chat_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat
    ADD CONSTRAINT chat_pkey PRIMARY KEY (chat_id);


--
-- Name: chat_user_role chat_user_role_new_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user_role
    ADD CONSTRAINT chat_user_role_new_pkey PRIMARY KEY (user_id, chat_id, chat_role);


--
-- Name: file file_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (file_id);


--
-- Name: friends friends_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (my_id, friend_id);


--
-- Name: message_file message_file_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_file
    ADD CONSTRAINT message_file_pkey PRIMARY KEY (message_id, file_id);


--
-- Name: message message_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (message_id);


--
-- Name: message_seen message_view_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_seen
    ADD CONSTRAINT message_view_pkey PRIMARY KEY (message_id, user_id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (token_id);


--
-- Name: server_channel server_channel_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel
    ADD CONSTRAINT server_channel_pkey PRIMARY KEY (channel_id);


--
-- Name: server server_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server
    ADD CONSTRAINT server_pkey PRIMARY KEY (server_id);


--
-- Name: server_role_action server_role_action_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_role_action
    ADD CONSTRAINT server_role_action_pkey PRIMARY KEY (server_id, role_name, role_action);


--
-- Name: server_role server_role_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_role
    ADD CONSTRAINT server_role_pkey PRIMARY KEY (server_id, role_name);


--
-- Name: server_user server_user_new_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user
    ADD CONSTRAINT server_user_new_pkey PRIMARY KEY (user_id, server_id);


--
-- Name: server_user_role server_user_role_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user_role
    ADD CONSTRAINT server_user_role_pkey PRIMARY KEY (server_id, role_name, user_id);


--
-- Name: user_app_role user_app_role_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.user_app_role
    ADD CONSTRAINT user_app_role_pkey PRIMARY KEY (user_id, app_role_id);


--
-- Name: user user_email_key; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- Name: user user_username_key; Type: CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy."user"
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- Name: chat chat_picture_id_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat
    ADD CONSTRAINT chat_picture_id_fkey FOREIGN KEY (picture_id) REFERENCES convy.file(file_id);


--
-- Name: chat_user fk_chat; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user
    ADD CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES convy.chat(chat_id) ON DELETE CASCADE;


--
-- Name: chat_user_role fk_chat; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user_role
    ADD CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES convy.chat(chat_id) ON DELETE CASCADE;


--
-- Name: server_channel fk_chat; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel
    ADD CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES convy.chat(chat_id);


--
-- Name: message_file fk_file; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_file
    ADD CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES convy.file(file_id) ON DELETE CASCADE;


--
-- Name: message_file fk_message; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_file
    ADD CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES convy.message(message_id) ON DELETE CASCADE;


--
-- Name: message_seen fk_message; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_seen
    ADD CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES convy.message(message_id) ON DELETE CASCADE;


--
-- Name: message fk_message_chat; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message
    ADD CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) REFERENCES convy.chat(chat_id) ON DELETE CASCADE;


--
-- Name: message fk_message_sender; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message
    ADD CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: server_role fk_server; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_role
    ADD CONSTRAINT fk_server FOREIGN KEY (server_id) REFERENCES convy.server(server_id);


--
-- Name: server_user fk_server; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user
    ADD CONSTRAINT fk_server FOREIGN KEY (server_id) REFERENCES convy.server(server_id);


--
-- Name: server_channel fk_server; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_channel
    ADD CONSTRAINT fk_server FOREIGN KEY (server_id) REFERENCES convy.server(server_id);


--
-- Name: server_user_role fk_server_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user_role
    ADD CONSTRAINT fk_server_user FOREIGN KEY (server_id) REFERENCES convy.server(server_id);


--
-- Name: message_seen fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message_seen
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: refresh_token fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.refresh_token
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: chat_user fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: user_app_role fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.user_app_role
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: chat_user_role fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.chat_user_role
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: server_user fk_user; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server_user
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: friends friends_user_id_1_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.friends
    ADD CONSTRAINT friends_user_id_1_fkey FOREIGN KEY (my_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: friends friends_user_id_2_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.friends
    ADD CONSTRAINT friends_user_id_2_fkey FOREIGN KEY (friend_id) REFERENCES convy."user"(user_id) ON DELETE CASCADE;


--
-- Name: message message_replying_message_id_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.message
    ADD CONSTRAINT message_replying_message_id_fkey FOREIGN KEY (replying_message_id) REFERENCES convy.message(message_id) ON DELETE CASCADE;


--
-- Name: server server_picture_id_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.server
    ADD CONSTRAINT server_picture_id_fkey FOREIGN KEY (picture_id) REFERENCES convy.file(file_id);


--
-- Name: user_app_role user_app_role_app_role_id_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy.user_app_role
    ADD CONSTRAINT user_app_role_app_role_id_fkey FOREIGN KEY (app_role_id) REFERENCES convy.app_role(app_role_id) ON DELETE CASCADE;


--
-- Name: user user_picture_id_fkey; Type: FK CONSTRAINT; Schema: convy; Owner: postgres
--

ALTER TABLE ONLY convy."user"
    ADD CONSTRAINT user_picture_id_fkey FOREIGN KEY (picture_id) REFERENCES convy.file(file_id);


--
-- PostgreSQL database dump complete
--

--
-- Database "postgres" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.6 (Debian 15.6-1.pgdg120+2)
-- Dumped by pg_dump version 15.6 (Debian 15.6-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE postgres;
--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE postgres OWNER TO postgres;

\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database cluster dump complete
--

