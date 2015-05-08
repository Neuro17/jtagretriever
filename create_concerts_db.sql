#drop database `concerts_db`;

create database `concerts_db`;

create table `concerts_db`.`artists`(
	`artist_id` varchar(255),
    `artist_name` varchar(255) not null,
    primary key (`artist_name`)
);

create table `concerts_db`.`artists_searched`(
	`artist_name` varchar(255) not null,
    `total` int default 0,
    primary key (`artist_name`)
);

create table `concerts_db`.`venues`(
	`venue_id` int,
    `latitude` double,
    `longitude` double,
    `venue_name` varchar(255),
    `country` varchar(255),
    `city` varchar(255),
    `region` varchar(255),
    primary key(`latitude`,`longitude`)
);

#non metto foreign key verso venues perchè ho già verificato che almeno una venue è presente nel db
create table `concerts_db`.`venues_searched`(
    `venue_name` varchar(255) not null,
    `total` int default 0,
    primary key(`venue_name`)
);

create table `concerts_db`.`events_table`(
		`event_id` int not null,
        `title` varchar(255),
        `datetime` timestamp,
		`description` varchar(255),
        `latitude` double not null,
        `longitude` double not null,
        primary key (`event_id`)
);

create table `concerts_db`.`events_table_searched`(
        `title` varchar(255),
        `total` int default 0,
        primary key (`title`)
);

alter table `concerts_db`.`events_table` 
add constraint `FK_venues` 
foreign key (`latitude`,`longitude`) 
references `concerts_db`.`venues`(`latitude`,`longitude`) on delete cascade;

create table `concerts_db`.`partecipations`(
	`event_id` int not null,
	`artist_name` varchar(255) not null,
	primary key (`event_id`,`artist_name`)	
);

alter table `concerts_db`.`partecipations` 
add constraint `FK_events_table` 
foreign key (`event_id`) references `concerts_db`.`events_table`(`event_id`) on delete cascade;

alter table `concerts_db`.`partecipations` 
add constraint `FK_artists` 
foreign key (`artist_name`) references `concerts_db`.`artists`(`artist_name`) on delete cascade;

select * from `concerts_db`.`artists`;
select * from `concerts_db`.`venues`;
select * from `concerts_db`.`events_table`;
select * from `concerts_db`.`partecipations`;
select * from `concerts_db`.`artists_searched`;
select * from `concerts_db`.`venues_searched`;
select * from `concerts_db`.`events_table_searched`;

select * from `concerts_db`.`venues` where `venue_name` like '%Foo%';
select * from `concerts_db`.`artists` where `artist_name` like '%Lollapalooza%';
select * from `concerts_db`.`events_table` where `title` like '%Alt%';

INSERT INTO `concerts_db`.`events_table_searched`(`title`, `total`)
    VALUES ('telo',1)
        ON DUPLICATE KEY UPDATE `total` = `total` + 1;
        
select * from `concerts_db`.`artists_searched` ORDER BY `total` DESC LIMIT 10;
select * from `concerts_db`.`venues_searched` ORDER BY `total` DESC LIMIT 10;