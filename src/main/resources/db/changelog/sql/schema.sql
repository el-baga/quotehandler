--
-- PostgreSQL database dump
--

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: quote; Type: TABLE; Schema: public
--

CREATE TABLE public.quote (
    id bigint NOT NULL,
    isin character varying(255),
    bid double precision,
    ask double precision
);

--
-- Name: quote_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.quote_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: quote_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.quote_id_seq OWNED BY public.quote.id;

--
-- Name: energy_lvl; Type: TABLE; Schema: public
--

CREATE TABLE public.energy_lvl (
    id bigint NOT NULL,
    best_price double precision,
    quote_id bigint
);


--
-- Name: energy_lvl_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.energy_lvl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: energy_lvl_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.energy_lvl_id_seq OWNED BY public.energy_lvl.id;

--
-- Name: quote id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.quote ALTER COLUMN id SET DEFAULT nextval('public.quote_id_seq'::regclass);

--
-- Name: energy_lvl id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.energy_lvl ALTER COLUMN id SET DEFAULT nextval('public.energy_lvl_id_seq'::regclass);

ALTER TABLE ONLY public.quote
    ADD CONSTRAINT quote_pkey PRIMARY KEY (id);


--
-- Name: page energy_lvl_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.energy_lvl
    ADD CONSTRAINT energy_lvl_pkey PRIMARY KEY (id);

--
-- Name: energy_lvl fk_energy_lvl_quote_id; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.energy_lvl
    ADD CONSTRAINT fk_energy_lvl_quote_id FOREIGN KEY (quote_id) REFERENCES public.quote(id);