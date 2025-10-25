import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const name = ref("");
  const departmentName = ref("");
  const roleName = ref("");
  const admin = ref(false);
  const status = ref(0);

  const setUser = (data) => {
    name.value = data.name;
    departmentName.value = data.departmentName;
    roleName.value = data.roleName;
    admin.value = data.admin;
    status.value = data.status;
  };

  return { name, departmentName, roleName, admin, status, setUser };
});
