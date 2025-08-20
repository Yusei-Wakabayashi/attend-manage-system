<script setup>
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";

const route = useRoute()

const props = defineProps({
  requests: Array,
  selectedRequest: String,
});

const emit = defineEmits(["update:selectedRequest"]);

const localSelectedRequest = ref(props.selectedRequest);

// 親コンポーネントから渡された値が変わったときに子の値を更新（親 → 子）
watch(() => props.selectedRequest, (val) => (localSelectedRequest.value = val));

// 子コンポーネント内の値が変わったときに親に変更通知（子 → 親）
watch(localSelectedRequest, (val) => emit("update:selectedRequest", val));
</script>
<template>
  <div>
    <label class="block font-semibold md:mb-2">{{ route.path !== "/vacation" ? "遅刻、早退、外出選択" : "休暇種類選択" }}</label>
    <select
      v-model="localSelectedRequest"
      class="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-green-200 hover:ring hover:ring-green-200"
    >
      <option disabled :value="null">選択してください</option>
      <option v-for="(request, index) in requests" :key="index">
        {{ request }}
      </option>
    </select>
  </div>
</template>