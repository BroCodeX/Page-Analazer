@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
content = @`
    <main class="mx-auto my-5 p-5 bg-white rounded shadow text-left" style="max-width: 1200px; width: 90%;">
        <h1 class="fs-1 mb-4 text-dark">Сайт: ${page.getUrlModel().getName()}</h1>

        <table class="table table-bordered table-hover mt-4">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrlModel().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrlModel().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrlModel().getFormattedDate()}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="fs-1 mb-5 mt-5 text-dark">Проверки</h2>
        <form method="post" action="${NamedRoutes.checksPath(page.getUrlModel().getId())}">
            <button type="submit" class="btn btn-success btn-lg px-5">Запустить проверку</button>
        </form>

        <table class="table table-hover mt-4">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Код ответа</th>
                <th>Title</th>
                <th>H1</th>
                <th>Description</th>
                <th>Дата проверки</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${page.getUrlModel().getId()}</td>
                <td>Код ответа</td>
                <td>Title</td>
                <td>H1</td>
                <td>Description</td>
                <td>Дата проверки</td>
            </tr>
            </tbody>
        </table>
    </main>

    <script>
        document.querySelectorAll('nav a').forEach(link => {
            link.addEventListener('mouseover', () => {
                link.style.transform = 'scale(1.05)';
                link.style.transition = 'transform 0.2s ease';
            });
            link.addEventListener('mouseout', () => {
                link.style.transform = 'scale(1)';
                link.style.transition = 'transform 0.2s ease';
            });
        });
    </script>
`
)
