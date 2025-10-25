FROM node:22-slim

WORKDIR /app

# package.json と lock ファイルだけ先にコピー
COPY package*.json ./

# lockファイルに従って依存関係をインストール
RUN npm ci
# @tailwindcssをインストール
RUN npm install -D tailwindcss@latest @tailwindcss/vite

RUN npm install pinia

# ソースコードをコピー
COPY . .

EXPOSE 5173

# Vite 開発サーバーを起動
CMD ["npm", "run", "dev", "--", "--host"]
