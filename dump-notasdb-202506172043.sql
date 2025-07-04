PGDMP      +                }            notasdb    17.4 (Debian 17.4-1.pgdg120+2)    17.0 N    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16408    notasdb    DATABASE     r   CREATE DATABASE notasdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE notasdb;
                     postgres    false            �            1259    32962 	   actividad    TABLE       CREATE TABLE public.actividad (
    fecha_actividad date NOT NULL,
    id_actividad integer NOT NULL,
    id_parcial integer NOT NULL,
    valor_maximo double precision,
    tipo_actividad character varying(20) NOT NULL,
    descripcion character varying(200)
);
    DROP TABLE public.actividad;
       public         heap r       postgres    false            �            1259    32961    actividad_id_actividad_seq    SEQUENCE     �   ALTER TABLE public.actividad ALTER COLUMN id_actividad ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.actividad_id_actividad_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    219            �            1259    32968    anio_lectivo    TABLE     �   CREATE TABLE public.anio_lectivo (
    fecha_final date NOT NULL,
    fecha_inicio date NOT NULL,
    id_aniolectivo integer NOT NULL,
    estado_lectivo character varying(255) NOT NULL
);
     DROP TABLE public.anio_lectivo;
       public         heap r       postgres    false            �            1259    32967    anio_lectivo_id_aniolectivo_seq    SEQUENCE     �   ALTER TABLE public.anio_lectivo ALTER COLUMN id_aniolectivo ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.anio_lectivo_id_aniolectivo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    221            �            1259    24600    anio_lectivo_id_seq    SEQUENCE     |   CREATE SEQUENCE public.anio_lectivo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.anio_lectivo_id_seq;
       public               postgres    false            �            1259    32974    calificacion    TABLE     �   CREATE TABLE public.calificacion (
    id_actividad integer NOT NULL,
    id_calificacion integer NOT NULL,
    nota double precision NOT NULL,
    id_usuario character varying(10) NOT NULL,
    comentario character varying(200)
);
     DROP TABLE public.calificacion;
       public         heap r       postgres    false            �            1259    32973     calificacion_id_calificacion_seq    SEQUENCE     �   ALTER TABLE public.calificacion ALTER COLUMN id_calificacion ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.calificacion_id_calificacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    223            �            1259    32980    curso    TABLE     �   CREATE TABLE public.curso (
    id_aniolectivo integer NOT NULL,
    id_curso integer NOT NULL,
    nombre_curso character varying(15) NOT NULL
);
    DROP TABLE public.curso;
       public         heap r       postgres    false            �            1259    32979    curso_id_curso_seq    SEQUENCE     �   ALTER TABLE public.curso ALTER COLUMN id_curso ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.curso_id_curso_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    225            �            1259    32985    curso_materia    TABLE     f   CREATE TABLE public.curso_materia (
    id_curso integer NOT NULL,
    id_materia integer NOT NULL
);
 !   DROP TABLE public.curso_materia;
       public         heap r       postgres    false            �            1259    32991    materia    TABLE     t   CREATE TABLE public.materia (
    id_materia integer NOT NULL,
    nombre_materia character varying(30) NOT NULL
);
    DROP TABLE public.materia;
       public         heap r       postgres    false            �            1259    32990    materia_id_materia_seq    SEQUENCE     �   ALTER TABLE public.materia ALTER COLUMN id_materia ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.materia_id_materia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    228            �            1259    32997    notificaciones    TABLE     �   CREATE TABLE public.notificaciones (
    fecha_notificacion date NOT NULL,
    id_notificacion integer NOT NULL,
    id_usuario character varying(10) NOT NULL,
    mensaje_notificacion character varying(100) NOT NULL
);
 "   DROP TABLE public.notificaciones;
       public         heap r       postgres    false            �            1259    32996 "   notificaciones_id_notificacion_seq    SEQUENCE     �   ALTER TABLE public.notificaciones ALTER COLUMN id_notificacion ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.notificaciones_id_notificacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    230            �            1259    33003    parcial    TABLE     �   CREATE TABLE public.parcial (
    fecha_fin date NOT NULL,
    fecha_inicio date NOT NULL,
    id_materia integer NOT NULL,
    id_parcial integer NOT NULL,
    numero_parcial integer NOT NULL
);
    DROP TABLE public.parcial;
       public         heap r       postgres    false            �            1259    33002    parcial_id_parcial_seq    SEQUENCE     �   ALTER TABLE public.parcial ALTER COLUMN id_parcial ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.parcial_id_parcial_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    232            �            1259    33009    promediogeneralestudiante    TABLE       CREATE TABLE public.promediogeneralestudiante (
    comportamiento character varying(1) NOT NULL,
    id_curso integer NOT NULL,
    id_promedio_general integer NOT NULL,
    promedio_general double precision NOT NULL,
    id_usuario character varying(10) NOT NULL
);
 -   DROP TABLE public.promediogeneralestudiante;
       public         heap r       postgres    false            �            1259    33008 1   promediogeneralestudiante_id_promedio_general_seq    SEQUENCE       ALTER TABLE public.promediogeneralestudiante ALTER COLUMN id_promedio_general ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.promediogeneralestudiante_id_promedio_general_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    234            �            1259    33015    rol    TABLE     h   CREATE TABLE public.rol (
    id_rol integer NOT NULL,
    nombre_rol character varying(20) NOT NULL
);
    DROP TABLE public.rol;
       public         heap r       postgres    false            �            1259    33014    rol_id_rol_seq    SEQUENCE     �   ALTER TABLE public.rol ALTER COLUMN id_rol ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.rol_id_rol_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    236            �            1259    33020    usuario    TABLE     T  CREATE TABLE public.usuario (
    email_verificado boolean NOT NULL,
    mfa_habilitado boolean NOT NULL,
    mfa_code_expiration timestamp(6) without time zone,
    id_usuario character varying(10) NOT NULL,
    estado_usuario character varying(20) NOT NULL,
    nickname_usuario character varying(30) NOT NULL,
    token_verificacion character varying(36),
    apellidos_usuario character varying(50) NOT NULL,
    nombres_usuario character varying(50) NOT NULL,
    contrasena_usuario character varying(255) NOT NULL,
    email character varying(255),
    mfa_secret character varying(255)
);
    DROP TABLE public.usuario;
       public         heap r       postgres    false            �            1259    33029    usuario_curso    TABLE     u   CREATE TABLE public.usuario_curso (
    id_curso integer NOT NULL,
    id_usuario character varying(255) NOT NULL
);
 !   DROP TABLE public.usuario_curso;
       public         heap r       postgres    false            �            1259    33034    usuario_rol    TABLE     q   CREATE TABLE public.usuario_rol (
    id_rol integer NOT NULL,
    id_usuario character varying(255) NOT NULL
);
    DROP TABLE public.usuario_rol;
       public         heap r       postgres    false            �          0    32962 	   actividad 
   TABLE DATA           y   COPY public.actividad (fecha_actividad, id_actividad, id_parcial, valor_maximo, tipo_actividad, descripcion) FROM stdin;
    public               postgres    false    219   �d       �          0    32968    anio_lectivo 
   TABLE DATA           a   COPY public.anio_lectivo (fecha_final, fecha_inicio, id_aniolectivo, estado_lectivo) FROM stdin;
    public               postgres    false    221   e       �          0    32974    calificacion 
   TABLE DATA           c   COPY public.calificacion (id_actividad, id_calificacion, nota, id_usuario, comentario) FROM stdin;
    public               postgres    false    223   Je       �          0    32980    curso 
   TABLE DATA           G   COPY public.curso (id_aniolectivo, id_curso, nombre_curso) FROM stdin;
    public               postgres    false    225   ge       �          0    32985    curso_materia 
   TABLE DATA           =   COPY public.curso_materia (id_curso, id_materia) FROM stdin;
    public               postgres    false    226   �e       �          0    32991    materia 
   TABLE DATA           =   COPY public.materia (id_materia, nombre_materia) FROM stdin;
    public               postgres    false    228   �e       �          0    32997    notificaciones 
   TABLE DATA           o   COPY public.notificaciones (fecha_notificacion, id_notificacion, id_usuario, mensaje_notificacion) FROM stdin;
    public               postgres    false    230   .f       �          0    33003    parcial 
   TABLE DATA           b   COPY public.parcial (fecha_fin, fecha_inicio, id_materia, id_parcial, numero_parcial) FROM stdin;
    public               postgres    false    232   Kf       �          0    33009    promediogeneralestudiante 
   TABLE DATA           �   COPY public.promediogeneralestudiante (comportamiento, id_curso, id_promedio_general, promedio_general, id_usuario) FROM stdin;
    public               postgres    false    234   hf       �          0    33015    rol 
   TABLE DATA           1   COPY public.rol (id_rol, nombre_rol) FROM stdin;
    public               postgres    false    236   �f       �          0    33020    usuario 
   TABLE DATA           �   COPY public.usuario (email_verificado, mfa_habilitado, mfa_code_expiration, id_usuario, estado_usuario, nickname_usuario, token_verificacion, apellidos_usuario, nombres_usuario, contrasena_usuario, email, mfa_secret) FROM stdin;
    public               postgres    false    237   �f       �          0    33029    usuario_curso 
   TABLE DATA           =   COPY public.usuario_curso (id_curso, id_usuario) FROM stdin;
    public               postgres    false    238   h       �          0    33034    usuario_rol 
   TABLE DATA           9   COPY public.usuario_rol (id_rol, id_usuario) FROM stdin;
    public               postgres    false    239   ;h       �           0    0    actividad_id_actividad_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.actividad_id_actividad_seq', 1, false);
          public               postgres    false    218            �           0    0    anio_lectivo_id_aniolectivo_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.anio_lectivo_id_aniolectivo_seq', 1, true);
          public               postgres    false    220            �           0    0    anio_lectivo_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.anio_lectivo_id_seq', 4, true);
          public               postgres    false    217            �           0    0     calificacion_id_calificacion_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.calificacion_id_calificacion_seq', 1, false);
          public               postgres    false    222            �           0    0    curso_id_curso_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.curso_id_curso_seq', 6, true);
          public               postgres    false    224            �           0    0    materia_id_materia_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.materia_id_materia_seq', 4, true);
          public               postgres    false    227            �           0    0 "   notificaciones_id_notificacion_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.notificaciones_id_notificacion_seq', 1, false);
          public               postgres    false    229            �           0    0    parcial_id_parcial_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.parcial_id_parcial_seq', 1, false);
          public               postgres    false    231            �           0    0 1   promediogeneralestudiante_id_promedio_general_seq    SEQUENCE SET     `   SELECT pg_catalog.setval('public.promediogeneralestudiante_id_promedio_general_seq', 1, false);
          public               postgres    false    233            �           0    0    rol_id_rol_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.rol_id_rol_seq', 4, true);
          public               postgres    false    235            �           2606    32966    actividad actividad_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.actividad
    ADD CONSTRAINT actividad_pkey PRIMARY KEY (id_actividad);
 B   ALTER TABLE ONLY public.actividad DROP CONSTRAINT actividad_pkey;
       public                 postgres    false    219            �           2606    32972    anio_lectivo anio_lectivo_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.anio_lectivo
    ADD CONSTRAINT anio_lectivo_pkey PRIMARY KEY (id_aniolectivo);
 H   ALTER TABLE ONLY public.anio_lectivo DROP CONSTRAINT anio_lectivo_pkey;
       public                 postgres    false    221            �           2606    32978    calificacion calificacion_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.calificacion
    ADD CONSTRAINT calificacion_pkey PRIMARY KEY (id_calificacion);
 H   ALTER TABLE ONLY public.calificacion DROP CONSTRAINT calificacion_pkey;
       public                 postgres    false    223            �           2606    32989     curso_materia curso_materia_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.curso_materia
    ADD CONSTRAINT curso_materia_pkey PRIMARY KEY (id_curso, id_materia);
 J   ALTER TABLE ONLY public.curso_materia DROP CONSTRAINT curso_materia_pkey;
       public                 postgres    false    226    226            �           2606    32984    curso curso_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.curso
    ADD CONSTRAINT curso_pkey PRIMARY KEY (id_curso);
 :   ALTER TABLE ONLY public.curso DROP CONSTRAINT curso_pkey;
       public                 postgres    false    225            �           2606    32995    materia materia_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.materia
    ADD CONSTRAINT materia_pkey PRIMARY KEY (id_materia);
 >   ALTER TABLE ONLY public.materia DROP CONSTRAINT materia_pkey;
       public                 postgres    false    228            �           2606    49177    curso nombre_curso_unique 
   CONSTRAINT     \   ALTER TABLE ONLY public.curso
    ADD CONSTRAINT nombre_curso_unique UNIQUE (nombre_curso);
 C   ALTER TABLE ONLY public.curso DROP CONSTRAINT nombre_curso_unique;
       public                 postgres    false    225            �           2606    33001 "   notificaciones notificaciones_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT notificaciones_pkey PRIMARY KEY (id_notificacion);
 L   ALTER TABLE ONLY public.notificaciones DROP CONSTRAINT notificaciones_pkey;
       public                 postgres    false    230            �           2606    33007    parcial parcial_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.parcial
    ADD CONSTRAINT parcial_pkey PRIMARY KEY (id_parcial);
 >   ALTER TABLE ONLY public.parcial DROP CONSTRAINT parcial_pkey;
       public                 postgres    false    232            �           2606    33013 8   promediogeneralestudiante promediogeneralestudiante_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.promediogeneralestudiante
    ADD CONSTRAINT promediogeneralestudiante_pkey PRIMARY KEY (id_promedio_general);
 b   ALTER TABLE ONLY public.promediogeneralestudiante DROP CONSTRAINT promediogeneralestudiante_pkey;
       public                 postgres    false    234            �           2606    33019    rol rol_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id_rol);
 6   ALTER TABLE ONLY public.rol DROP CONSTRAINT rol_pkey;
       public                 postgres    false    236            �           2606    33033     usuario_curso usuario_curso_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.usuario_curso
    ADD CONSTRAINT usuario_curso_pkey PRIMARY KEY (id_curso, id_usuario);
 J   ALTER TABLE ONLY public.usuario_curso DROP CONSTRAINT usuario_curso_pkey;
       public                 postgres    false    238    238            �           2606    33028 $   usuario usuario_nickname_usuario_key 
   CONSTRAINT     k   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_nickname_usuario_key UNIQUE (nickname_usuario);
 N   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_nickname_usuario_key;
       public                 postgres    false    237            �           2606    33026    usuario usuario_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);
 >   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_pkey;
       public                 postgres    false    237            �           2606    33038    usuario_rol usuario_rol_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.usuario_rol
    ADD CONSTRAINT usuario_rol_pkey PRIMARY KEY (id_rol, id_usuario);
 F   ALTER TABLE ONLY public.usuario_rol DROP CONSTRAINT usuario_rol_pkey;
       public                 postgres    false    239    239            �           2606    33049 (   calificacion fk2g59r7b3wkdi3cqicjrh1p82g    FK CONSTRAINT     �   ALTER TABLE ONLY public.calificacion
    ADD CONSTRAINT fk2g59r7b3wkdi3cqicjrh1p82g FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);
 R   ALTER TABLE ONLY public.calificacion DROP CONSTRAINT fk2g59r7b3wkdi3cqicjrh1p82g;
       public               postgres    false    237    223    3292            �           2606    33054 !   curso fk3eim2t6bc7l3nh51y6x87b9p9    FK CONSTRAINT     �   ALTER TABLE ONLY public.curso
    ADD CONSTRAINT fk3eim2t6bc7l3nh51y6x87b9p9 FOREIGN KEY (id_aniolectivo) REFERENCES public.anio_lectivo(id_aniolectivo);
 K   ALTER TABLE ONLY public.curso DROP CONSTRAINT fk3eim2t6bc7l3nh51y6x87b9p9;
       public               postgres    false    225    221    3270            �           2606    33099 '   usuario_rol fk3ftpt75ebughsiy5g03b11akt    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuario_rol
    ADD CONSTRAINT fk3ftpt75ebughsiy5g03b11akt FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);
 Q   ALTER TABLE ONLY public.usuario_rol DROP CONSTRAINT fk3ftpt75ebughsiy5g03b11akt;
       public               postgres    false    3292    237    239            �           2606    33079 5   promediogeneralestudiante fka1bjfq171h6yxt2k309lega0x    FK CONSTRAINT     �   ALTER TABLE ONLY public.promediogeneralestudiante
    ADD CONSTRAINT fka1bjfq171h6yxt2k309lega0x FOREIGN KEY (id_curso) REFERENCES public.curso(id_curso);
 _   ALTER TABLE ONLY public.promediogeneralestudiante DROP CONSTRAINT fka1bjfq171h6yxt2k309lega0x;
       public               postgres    false    225    234    3274            �           2606    33069 *   notificaciones fkcyyx705r6bbei53suyawwbv7j    FK CONSTRAINT     �   ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT fkcyyx705r6bbei53suyawwbv7j FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);
 T   ALTER TABLE ONLY public.notificaciones DROP CONSTRAINT fkcyyx705r6bbei53suyawwbv7j;
       public               postgres    false    230    3292    237            �           2606    33044 (   calificacion fkdwb7wofltkiw5gvuj34lbwb64    FK CONSTRAINT     �   ALTER TABLE ONLY public.calificacion
    ADD CONSTRAINT fkdwb7wofltkiw5gvuj34lbwb64 FOREIGN KEY (id_actividad) REFERENCES public.actividad(id_actividad);
 R   ALTER TABLE ONLY public.calificacion DROP CONSTRAINT fkdwb7wofltkiw5gvuj34lbwb64;
       public               postgres    false    223    3268    219            �           2606    33059 )   curso_materia fke92qkr6grt5s6ucvbqjbtpo9x    FK CONSTRAINT     �   ALTER TABLE ONLY public.curso_materia
    ADD CONSTRAINT fke92qkr6grt5s6ucvbqjbtpo9x FOREIGN KEY (id_curso) REFERENCES public.curso(id_curso);
 S   ALTER TABLE ONLY public.curso_materia DROP CONSTRAINT fke92qkr6grt5s6ucvbqjbtpo9x;
       public               postgres    false    225    3274    226            �           2606    33074 #   parcial fkhv71cgbcess86oqp2cntvf17m    FK CONSTRAINT     �   ALTER TABLE ONLY public.parcial
    ADD CONSTRAINT fkhv71cgbcess86oqp2cntvf17m FOREIGN KEY (id_materia) REFERENCES public.materia(id_materia);
 M   ALTER TABLE ONLY public.parcial DROP CONSTRAINT fkhv71cgbcess86oqp2cntvf17m;
       public               postgres    false    232    3280    228            �           2606    33094 '   usuario_rol fkkxcv7htfnm9x1wkofnud0ewql    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuario_rol
    ADD CONSTRAINT fkkxcv7htfnm9x1wkofnud0ewql FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol);
 Q   ALTER TABLE ONLY public.usuario_rol DROP CONSTRAINT fkkxcv7htfnm9x1wkofnud0ewql;
       public               postgres    false    239    236    3288            �           2606    33039 %   actividad fkmufi79go1gki8jiaamip1200d    FK CONSTRAINT     �   ALTER TABLE ONLY public.actividad
    ADD CONSTRAINT fkmufi79go1gki8jiaamip1200d FOREIGN KEY (id_parcial) REFERENCES public.parcial(id_parcial);
 O   ALTER TABLE ONLY public.actividad DROP CONSTRAINT fkmufi79go1gki8jiaamip1200d;
       public               postgres    false    3284    232    219            �           2606    33084 )   usuario_curso fkn3e4um8h77uo8to3r3dfbwe5a    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuario_curso
    ADD CONSTRAINT fkn3e4um8h77uo8to3r3dfbwe5a FOREIGN KEY (id_curso) REFERENCES public.curso(id_curso);
 S   ALTER TABLE ONLY public.usuario_curso DROP CONSTRAINT fkn3e4um8h77uo8to3r3dfbwe5a;
       public               postgres    false    225    3274    238            �           2606    33089 (   usuario_curso fkqape4xljvl90y098ouv7ohnf    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuario_curso
    ADD CONSTRAINT fkqape4xljvl90y098ouv7ohnf FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);
 R   ALTER TABLE ONLY public.usuario_curso DROP CONSTRAINT fkqape4xljvl90y098ouv7ohnf;
       public               postgres    false    238    3292    237            �           2606    33064 )   curso_materia fktb2idpg3qvv8jfna43xa6dm9m    FK CONSTRAINT     �   ALTER TABLE ONLY public.curso_materia
    ADD CONSTRAINT fktb2idpg3qvv8jfna43xa6dm9m FOREIGN KEY (id_materia) REFERENCES public.materia(id_materia);
 S   ALTER TABLE ONLY public.curso_materia DROP CONSTRAINT fktb2idpg3qvv8jfna43xa6dm9m;
       public               postgres    false    228    226    3280            �      x������ � �      �   )   x�3202�50�54�4202�5 !NCN��̲|�=... ��;      �      x������ � �      �   (   x�3�4�(�Wp�2�4���,C0��2�\�b���� ���      �   $   x�3�4�2�4bc 6�2�|c0m����� S�_      �   K   x�3��I�K/MT�T��,I-J,)-J�2��M,I�=��$39�˄�935/93�X�,��Z�e��R �=... �J      �      x������ � �      �      x������ � �      �      x������ � �      �   3   x�3�tL����,.)JL�/�2�(�OK-2�9]�KJS2�JR�b���� Y�/      �   F  x�}�K��0 �s�<c�&JXV|�/�T)�<���WͲ�'��d&�o��� �ذ��j��+�Y���=Ӳ����'��o(phɋJ&��� ��0ȕ03�d�N��Ձ�]8��ˌ�	#H�16��Ç�h�_��ム�P�J<Km�3T�P-�>�/��-�(^��_)�\`7yq{ i̞c=S�ń�b7����|����ѯ��г���%�5��`��=Xz��=T����v
K�)!�:BP�F�������rT�5?T�o�'qT���vݙ�����;Yq��2ڦ8�Oϧ�����s��}�}�^�$��=�n      �      x������ � �      �   1   x�ǹ  �:�������D��$=�@���֨?��V�� �
�     