@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page
@template.layout.page(
page = page,
content = @`
    <main class="mx-auto my-5 p-5 bg-white rounded shadow text-center" style="max-width: 900px;">
        <h2 class="fs-1 mb-4 text-dark">Анализатор страниц</h2>
        <p class="fs-5 mb-4 text-secondary">Бесплатно проверяйте сайты на SEO пригодность</p>
        <form action="${NamedRoutes.urlsPath()}" method="post">
            <div class="mb-3 w-100">
                <label class="w-100">
                    <input type="text" placeholder="Введите ссылку" required name="url" class="form-control form-control-lg w-100">
                </label>
                <p class="fs-6 text-muted mt-2">Пример: https://www.example.com</p>
            </div>
            <button type="submit" class="btn btn-success btn-lg px-5">
                Проверить
            </button>
        </form>
    </main>

    <script>
        document.querySelectorAll('nav a').forEach(link => {
            link.addEventListener('mouseover', () => {
                link.classList.add('scale-up');
            });
            link.addEventListener('mouseout', () => {
                link.classList.remove('scale-up');
            });
        });

        const button = document.querySelector('button[type="submit"]');
        button.addEventListener('mouseover', () => {
            button.classList.add('hover-effect');
        });
        button.addEventListener('mouseout', () => {
            button.classList.remove('hover-effect');
        });

        const input = document.querySelector('input[type="url"]');
        input.addEventListener('focus', () => {
            input.classList.add('border-success');
        });
        input.addEventListener('blur', () => {
            input.classList.remove('border-success');
        });
    </script>

    <style>
        .scale-up {
            transform: scale(1.05);
            transition: transform 0.2s;
        }
        .hover-effect {
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
            transform: translateY(-2px);
            transition: box-shadow 0.2s, transform 0.2s;
        }
        input[type="text"] {
            width: 100%;
        }
    </style>
`
)
