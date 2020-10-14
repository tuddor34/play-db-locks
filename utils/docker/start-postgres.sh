docker run -d \
    --name play_lock_container \
    -p 5430:5432 \
    -e POSTGRES_USER=play_db \
    -e POSTGRES_PASSWORD=lapsus \
    -e POSTGRES_DB=play_db \
    -v /home/tuddor/temp/play_db:/var/lib/postgresql/data \
    postgres -c log_statement=all