@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
    <main class="mx-auto my-5 p-5 bg-white rounded shadow text-center" style="max-width: 800px;">
        <h2 class="fs-1 mb-4 text-dark">Сайты</h2>

        <table class="table table-hover mt-4">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${page.getUrlModel().getId()}</td>
                <td>${page.getUrlModel().getName()}</td>
                <td>${page.getUrlModel().getCreatedAt().toString()}</td>
                <td>${page.getUrlModel().getId()}</td>
            </tr>
            </tbody>
        </table>
    </main>

    <script>
        document.querySelectorAll('nav a').forEach(link => {
            link.addEventListener('mouseover', () => {
                link.style.transform = 'scale(1.05)';
            });
            link.addEventListener('mouseout', () => {
                link.style.transform = 'scale(1)';
            });
        });
    </script>
`
)
