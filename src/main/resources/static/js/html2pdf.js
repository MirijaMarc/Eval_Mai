const exportBtn = document.querySelector(".exportBtn")
const contentClone = document.querySelector("#elementPDF").cloneNode(true);







exportBtn.addEventListener('click', ()=>{
    alert("kims")
    html2pdf(contentClone, pdfOptions)
});

const pdfOptions = {
    margin: 5,
    filename: "document.pdf",
    image: { type: "jpeg", quality: 1 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: "mm", format: "a4", orientation: "portrait" }
  };