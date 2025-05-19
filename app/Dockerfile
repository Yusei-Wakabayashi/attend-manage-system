# ベースイメージ（JDK）
FROM openjdk:17-jdk-slim
# 作業ディレクトリ作成
WORKDIR /app

# JARファイルをコンテナにコピー（ビルド済みJARを使う）
COPY target/*.jar app.jar

# ポート開放（例：8080）
EXPOSE 8080

# アプリ起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]
