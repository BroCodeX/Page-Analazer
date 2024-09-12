@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.util.Tools
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <main class="mx-auto my-5 p-5 bg-white rounded shadow text-left" style="max-width: 1200px; width: 90%;">
        <h1 class="fs-1 mb-4 text-dark">Сайт: ${page.getUrl().getName()}</h1>

        <table class="table table-bordered table-hover mt-4">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${Tools.getFormattedDate(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="fs-1 mb-5 mt-5 text-dark">Проверки</h2>
        <form method="post" action="${NamedRoutes.checksPath(page.getUrl().getId())}">
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
            @if(page.getCheckList() != null && !page.getCheckList().isEmpty())
                @for(var check : page.getCheckList())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${Tools.getFormattedDate(check.getCreatedAt())}</td>
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
