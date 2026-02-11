<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  requests: {
    type: Array,
    required: true
  },
  selectedRequest: Object
});

const emit = defineEmits(["update:selectedRequest"]);

// select 用に vacationTypeId を管理
const localSelectedId = ref(props.selectedRequest?.vacationTypeId || null);

// 親→子
watch(
  () => props.selectedRequest,
  (val) => (localSelectedId.value = val?.vacationTypeId || null)
);

// 子→親
watch(localSelectedId, (val) => {
  const selectedObj = props.requests.find(r => r.vacationTypeId === val) || null;
  emit("update:selectedRequest", selectedObj);
});
</script>

<template>
  <div>
    <label class="block font-semibold md:mb-2">休暇種類選択</label>
    <select v-model="localSelectedId">
      <option disabled :value="null">選択してください</option>
      <option
        v-for="request in requests"
        :key="request.vacationTypeId"
        :value="request.vacationTypeId"
      >
        {{ request.vacationTypeName }}
      </option>
    </select>
  </div>
</template>
