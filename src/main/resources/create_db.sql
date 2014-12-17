create database if not exists concerts;

create table if not exists concerts.artists(
	artist_name varchar(25) not null primary key,
	artist_id int unsigned,
	constraint unique(artist_id)
);

insert into concerts.artists values("metallica", 1);
insert into concerts.artists values("pearl jam", 2);

#drop table if exists concerts.venues;

create table if not exists concerts.venues(
	venue_id int unsigned not null primary key AUTO_INCREMENT,
	lat float not null,
	lng float not null,
	venue_name varchar(50) not null,
	country varchar(20) not null,
	city varchar(20) not null,
	constraint unique_lat_lng unique(lat, lng)
);

insert into concerts.venues (lat, lng, venue_name, country, city) values (43, 44, "prova", "prova", "prova");

#drop table if exists concerts.`events`;

create table if not exists concerts.`events`(
	event_id int unsigned not null primary key,
	title varchar(100),
	event_date varchar(25) not null,
	venue_id int unsigned not null,
	constraint foreign key (venue_id) references concerts.venues(venue_id)
);

create table if not exists concerts.performances(
	event_id int unsigned not null,
	artist_name varchar(25) not null,
	constraint primary key (event_id, artist_name)
);

alter table concerts.performances add constraint fk_event_id foreign key (event_id) references concerts.`events`(event_id);
alter table concerts.performances add constraint fk_artist_name foreign key (artist_name) references concerts.artists(artist_name);

#drop table if exists concerts.tags;

create table if not exists concerts.tags(
	tag varchar(50) not null,
	event_id int unsigned not null,
	count int unsigned not null default 1,
	constraint primary key (tag, event_id)
);

alter table concerts.tags add constraint fk_event foreign key (event_id) references concerts.`events`(event_id);