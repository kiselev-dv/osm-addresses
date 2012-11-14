CREATE TABLE cityes
(
  osm_id bigint NOT NULL,
  name character varying(250),
  "nameEN" character varying(250),
  "nameRU" character varying(250),
  province bigint,
  district character varying(250),
  geom geography(MultiPolygon,4326),
  CONSTRAINT city_pk PRIMARY KEY (osm_id )
)

CREATE TABLE city_triangles
(
  osmid bigint,
  geom geography(Polygon,4326)
)

CREATE TABLE buildings
(
  osm_id bigint NOT NULL,
  suburb character varying(250),
  quarter character varying(250),
  street character varying(250),
  "number" character varying(100),
  "addressRowType" character varying(250),
  city bigint,
  centroid point,
  CONSTRAINT buildings_city_fkey FOREIGN KEY (city)
      REFERENCES cityes (osm_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

CREATE TABLE states
(
  name character varying(250),
  "nameEN" character varying(250),
  "nameRU" character varying(250),
  osm_id bigint
)