-- USERS
create table if not exists public.users (
                                            id        bigserial primary key,
                                            email     text unique not null,
                                            password  text not null,
                                            full_name text
);

-- TAGS
create table if not exists public.tags (
                                           id   bigserial primary key,
                                           name text unique not null
);

-- LISTINGS (O2M: author_id → users.id)
create table if not exists public.listings (
                                               id         bigserial primary key,
                                               title      text not null,
                                               type       text not null,
                                               status     text not null,
                                               author_id  bigint not null references public.users(id) on delete cascade
);

-- M2M: listing ↔ tag
create table if not exists public.listing_tags (
                                                   listing_id bigint not null references public.listings(id) on delete cascade,
                                                   tag_id     bigint not null references public.tags(id)      on delete cascade,
                                                   primary key (listing_id, tag_id)
);

-- Индексы (полезно для производительности)
create index if not exists idx_listings_author        on public.listings(author_id);
create index if not exists idx_listing_tags_listing   on public.listing_tags(listing_id);
create index if not exists idx_listing_tags_tag       on public.listing_tags(tag_id);

-- (необязательно) несколько стартовых тегов
insert into public.tags(name)
select t from (values ('ремонт'), ('б/у'), ('даром'), ('запчасти'), ('инструменты')) v(t)
where not exists (select 1 from public.tags)
;

-- Фотографии объявлений
create table if not exists public.listing_photos (
                                                     id           bigserial primary key,
                                                     listing_id   bigint not null references public.listings(id) on delete cascade,
                                                     file_name    text   not null,  -- исходное имя
                                                     storage_path text   not null,  -- относительный путь на диске (listing/{id}/{uuid}.ext)
                                                     content_type text   not null,
                                                     size         bigint not null,
                                                     created_at   timestamp not null default now()
);

create index if not exists idx_listing_photos_listing on public.listing_photos(listing_id);

