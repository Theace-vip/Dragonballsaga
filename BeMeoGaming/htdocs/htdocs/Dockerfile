FROM php:8.2-apache

# Cài đặt các module cần thiết
RUN apt-get update && apt-get install -y \
    libzip-dev \
    zip \
    && docker-php-ext-install zip \
    && docker-php-ext-install mysqli \
    && docker-php-ext-enable mysqli \
    && docker-php-ext-install pdo pdo_mysql \
    && docker-php-ext-enable pdo pdo_mysql \
    && docker-php-ext-install sockets \
    && docker-php-ext-enable sockets
# Bật các module Apache cần thiết
RUN a2enmod rewrite
RUN a2enmod headers

# Copy cấu hình Apache tùy chỉnh
COPY apache.conf /etc/apache2/sites-available/000-default.conf

# Đảm bảo thư mục chứa mã nguồn có quyền phù hợp
RUN chown -R www-data:www-data /var/www/html/
RUN chmod -R 755 /var/www/html/

# Expose port 80
EXPOSE 80