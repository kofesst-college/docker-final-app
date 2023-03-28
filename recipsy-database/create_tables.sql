create table public.users
(
    id_user       serial
        primary key,
    login         varchar(100) not null
        unique,
    address       varchar(100) not null
        constraint users_username_key
            unique,
    nickname      varchar(100) not null,
    access_token  varchar      not null,
    token_salt    varchar      not null,
    hash_password varchar      not null
);

create table public.recipes
(
    id_recipe               serial
        constraint recipes_pk
            primary key,
    author_id               integer      not null
        constraint recipes_users_id_user_fk
            references public.users
            on delete cascade,
    cooking_duration_millis integer      not null,
    name                    varchar(100) not null,
    image_path              varchar(100)
);

create table public.cooking_stages
(
    id_stage    serial
        constraint cooking_stages_pk
            primary key,
    image_path  varchar(100),
    position    integer      not null,
    recipe_id   integer      not null
        constraint cooking_stages_recipes_id_recipe_fk
            references public.recipes
            on delete cascade,
    description varchar(255) not null
);

create table public.ingredients
(
    id_ingredient serial
        constraint ingredients_pk
            primary key,
    name          varchar(100)     not null,
    amount_type   varchar(20)      not null,
    amount_value  double precision not null,
    recipe_id     integer          not null
        constraint ingredients_recipes_id_recipe_fk
            references public.recipes
);