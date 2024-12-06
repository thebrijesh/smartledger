
    document.getElementById("download-template-btn").addEventListener("click", () => {
    fetch("/users/party/download-template")
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.style.display = "none";
            a.href = url;
            a.download = "template.xlsx"; // Set the filename
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            console.error("Error downloading the file:", error);
        });
});



    function handleFileUpload() {

        const file = document.getElementById("dropzone-file").files[0];
        console.log(file);
        if (!file) return;

        document.getElementById("upload-summary").classList.remove("hidden");
        document.getElementById("submit_btn").classList.add("hidden");
        document.getElementById("upload").classList.add("hidden");
        document.getElementById("fileName").textContent = document.getElementById("dropzone-file").files[0].name;
        document.getElementById("failure-reasons").textContent = "IN_PROGRESS";

        const formData = new FormData();
        formData.append("file", file);

        fetch("/users/party/upload-data", {
            method: "POST",
            body: formData,
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.status === "success") {
                    displaySummary(data.successfulContacts, data.failedContacts, data.failureReasons,data.status);
                } else {
                    displaySummary(0 ,0, data.message,data.status);
                    console.error("Error:", data.message);
                    alert("Upload failed. Please try again.");
                }
            })
            .catch((error) => {
                console.error("Error uploading file:", error);
            });
    }

    function displaySummary(successCount, failCount, reasons,status) {
          document.getElementById("success-count").textContent = successCount;
        document.getElementById("failure-count").textContent = failCount;
        if(status === "success") {
            document.getElementById("failure-reasons").textContent = "SUCCESS";
        }else{
            document.getElementById("failure-reasons").textContent = "FAILURE";
            reasons.forEach((reason) => {
                const failureReasonsContainer = document.getElementById("failure-reasons").appendChild(document.createElement("ul"));
                failureReasonsContainer.innerHTML = "";
                const li = document.createElement("li");
                li.textContent = reason;
                failureReasonsContainer.appendChild(li);
            });
        }
    }
    const dropzone = document.querySelector('label[for="dropzone-file"]');
    const fileInput = document.getElementById('dropzone-file');

    // Handle dragover event
    dropzone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropzone.classList.add('border-blue-500', 'bg-blue-50'); // Add border and background color change
    });

    // Handle dragleave event
    dropzone.addEventListener('dragleave', () => {
        dropzone.classList.remove('border-blue-500', 'bg-blue-50'); // Remove border and background color change
    });

    // Handle drop event
    dropzone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropzone.classList.remove('border-blue-500', 'bg-blue-50'); // Reset styling

        // Check if files are present in the drag event
        if (e.dataTransfer && e.dataTransfer.files.length > 0) {
            fileInput.files = e.dataTransfer.files; // Assign dropped files to the input element

            // Optional: Display the file name (can be customized)
            const fileName = e.dataTransfer.files[0].name;
            console.log('File dropped:', fileName);
        }
    });
