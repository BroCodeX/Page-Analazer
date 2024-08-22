@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <main class="mx-auto my-5 p-5 bg-white rounded shadow text-center" style="max-width: 1200px;">
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
            @for(var url : page.getUrlModels())
                <tr>
                    <td>${url.getId()}</td>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <td>${url.getCreatedAt().toString()}</td>
                    <td>${url.getId()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </main>

    <script>
        document.querySelectorAll('tbody tr').forEach(row => {
            row.addEventListener('mouseover', () => {
                row.classList.add('table-active');
            });
            row.addEventListener('mouseout', () => {
                row.classList.remove('table-active');
            });
        });
    </script>
`
)
