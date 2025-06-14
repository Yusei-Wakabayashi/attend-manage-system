# ベースイメージ（JDK）
FROM openjdk:17-jdk-slim
# 作業ディレクトリ作成
WORKDIR /app

# Maven をインストール（apt 経由）
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# ソースコードをコンテナにコピー
COPY . .
# targetフォルダが存在する場合パッケージの削除
RUN mvn clean
RUN mvn clean package
# ポート開放（例：8080）
EXPOSE 8080

# アプリ起動コマンド
ENTRYPOINT ["java", "-jar", "target/app.jar"]
