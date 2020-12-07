insert into region(name_en, name_ru, name_uzl, name_uzk)
values ('Tashkent', 'Ташкент', 'Toshkent', 'Тoшкент'),
       ('Andijan', 'Андижан', 'Andijon', 'Андижан');

insert into district(name_en, name_ru, name_uzl, name_uzk, region_id)
values ('Yakkasaray', 'Яккасарай', 'Yakkasaroy','Яккасарoй', 1),
       ('Xanabad', 'Ханабад', 'Xonobod','Хoнoбoд', 2);

insert into aware(name_en, name_ru, name_uzl, name_uzk)
values ('Telegram', 'Telegram', 'Telegram', 'Telegram'),
       ('Facebook', 'Facebook', 'Facebook', 'Facebook');

insert into role(role_name) values ('USER'),('RECEPTION'),('OPERATOR'),('DIRECTOR'),
                                   ('MODERATOR'),('ADMIN');
insert  into category (name_en, name_ru, name_uzk, name_uzl) values ('Bank','банк','банк','Bank'),('Shop','магазин','дукон','Dukon')
