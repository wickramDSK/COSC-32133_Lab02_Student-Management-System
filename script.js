document.addEventListener('DOMContentLoaded', () => {
    const studentForm = document.getElementById("studentForm");
    const studentTable = document
        .getElementById("studentTable")
        .getElementsByTagName("tbody")[0];
    const apiBaseUrl = "http://localhost:8080/api/students";
    function fetchStudents {
        fetch(apiBaseUrl)
            .then((response) => response.json())
            .then((data) => {
                studentTable.innerHTML = "";
                data.forEach((student) => {
                    const row = studentTable.insertRow();
                    row.innerHTML = `
    <td>${student.id}</td>
    <td>${student.firstName}</td>
    <td>${student.lastName}</td>
    <td>${student.email}</td>
    <td>${student.department}</td>
    <td>${student.yearOfEnrollment}</td>
    <td>
    <button
    onclick="editStudent(${student.id})">Edit</button>
    <button
    onclick="deleteStudent(${student.id})">Delete</button>
    </td>
    `;
                });
            });
    }
    studentForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("studentId").value;
        const firstName = document.getElementById("firstName").value;
        const lastName = document.getElementById("lastName").value;
        const email = document.getElementById("email").value;
        const department = document.getElementById("department").value;
        const yearOfEnrollment = document.getElementById("yearOfEnrollment").value;
        const method = id ? "PUT" : "POST";
        const url = id ? `${apiBaseUrl}/${id}` : apiBaseUrl;
        fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ firstName, lastName, email, department, yearOfEnrollment }),
        })
            .then((response) => response.json())
            .then(() => {
                studentForm.reset();
                fetchStudents();
            });
    });
    window.editStudent = function (id) {
        fetch(`${apiBaseUrl}/${id}`)
            .then((response) => response.json())
            .then((student) => {
                document.getElementById("studentId").value = student.id;
                document.getElementById("firstName").value = student.firstName;
                document.getElementById("lastName").value = student.lastName;
                document.getElementById("email").value = student.email;
                document.getElementById("department").value = student.department;
                document.getElementById("yearOfEnrollment").value = student.yearOfEnrollment;
            });
    };
    window.deleteStudent = function (id) {
        fetch(`${apiBaseUrl}/${id}`, {
            method: "DELETE",
        }).then(() => fetchStudents());
    };
    fetchStudents();
});