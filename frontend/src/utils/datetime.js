//ISO形式の日付文字列（例: "2025-07-01T09:00:00"）から「日付部分」だけを取り出す関数
export const formatDay = (isoString) => {
  return isoString?.includes("T") ? isoString.split("T")[0] : isoString;
};

//ISO形式の日付文字列（例: "2025-07-01T09:00:00"）から「時間部分（hh:mm）」だけを取り出す関数
export const formatTime = (isoString) => {
  return isoString?.includes("T") ? isoString.split("T")[1].slice(0, 5) : isoString;
};


//現在日時を yyyy/MM/ddTHH:mm:ss 形式で取得
export const getCurrentDateTime = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  const hours = String(today.getHours()).padStart(2, "0");
  const minutes = String(today.getMinutes()).padStart(2, "0");
  const seconds = String(today.getSeconds()).padStart(2, "0");
  return `${year}/${month}/${day}T${hours}:${minutes}:${seconds}`;
};

//現在日付を yyyy-MM-dd 形式で取得（日付選択に使う用）
export const getCurrentDate = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

//yyyy-MM-ddからyyyy/MM/ddに変換（API送信用）
export const convertToApiDate = (dateStr) => {
  const parts = dateStr.split("-");
  return `${parts[0]}/${parts[1]}/${parts[2]}`;
};