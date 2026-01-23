package com.example.springboot.json;

import java.io.IOException;

import com.example.springboot.dto.response.ArrayResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ArrayResponseSerializer<T> extends StdSerializer<ArrayResponse<T>>
{
    // デフォルトコンストラクタを定義
    public ArrayResponseSerializer()
    {
        this(null);
    }
    // 型解決のためにより汎用的な型を返すため作成
    public ArrayResponseSerializer(Class<ArrayResponse<T>> t)
    {
        super(t);
    }

    @Override
    // jsonでのレスポンスを作成
    public void serialize(ArrayResponse<T> value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        // jsonを作成開始
        gen.writeStartObject();
        // statusというキー名でフィールドに取得した数値を出力
        gen.writeNumberField("status", value.getStatus());
        // 入力された任意の値でキー名を指定、なければlistを指定
        String key = value.getKey() != null ? value.getKey() : "list";
        gen.writeObjectField(key, value.getList());
        // jsonの作成終了
        gen.writeEndObject();
    }
}
