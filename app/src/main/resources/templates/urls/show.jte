@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
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
            @if(page.getUrlModel().getUrlChecksId() != null && !page.getUrlModel().getUrlChecksId().isEmpty())
                @for(var check : page.getUrlModel().getUrlChecksId())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${check.getFormattedDate()}</td>
                    </tr>
                @endfor
            @endif
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
