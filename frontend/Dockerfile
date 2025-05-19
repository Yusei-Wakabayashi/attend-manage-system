FROM node:18-slim

WORKDIR /app

# Vite 初期化ツールをインストール
RUN npm install -g create-vite

# Vite + Vue のプロジェクトを生成（TypeScript にしたければ vue-ts に）
RUN create-vite . --template vue-ts

# 依存関係インストール
RUN npm install

RUN apt-get update

# ポート公開
EXPOSE 5173

# ホットリロード可能にする（--host指定で外部アクセスも可能）
CMD ["npm", "run", "dev", "--", "--host"]
