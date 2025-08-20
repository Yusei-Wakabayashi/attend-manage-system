<script setup>
import { ref, watch } from "vue";
import { useRoute } from "vue-router";

const props = defineProps({
  reasonText: String,
});

const emit = defineEmits(["update:reasonText"]);

const localReason = ref(props.reasonText);

//親コンポーネント内の値が変わったときに子に変更を通知する
watch(() => props.reasonText, (value) => {
  localReason.value = value;
});

//テキストエリアで入力があるたびに発火して、親に値を伝える
const onInput = (e) => {
  emit("update:reasonText", e.target.value);
};

const route = useRoute()
const isOvertime = route.path === "/overtime";
</script>

<!--申請理由コンポーネント-->
<template>
  <div>
    <label class="block font-semibold md:mb-2">
      申請理由
      <span :class="isOvertime ? 'text-red-500' : 'text-black'">
        ({{ isOvertime ? '必須' : '任意' }})
      </span>
    </label>
    <!--@inputはユーザーが入力するたび発火する-->
    <textarea
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
      rows="3"
      placeholder="申請理由を入力してください"
      @input="onInput"
    ></textarea>
  </div>
</template>
