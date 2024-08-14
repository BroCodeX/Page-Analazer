@template.layout.page(
content = @`
    <main style="max-width: 800px; margin: 50px auto; padding: 60px 40px; background-color: white; border-radius: 10px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); text-align: center;">
        <h2 style="font-size: 36px; margin-bottom: 30px; color: #3a3f44;">Сайты</h2>

        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
            <tr style="background-color: #f3f4f6; color: #3a3f44; text-align: left;">
                <th style="padding: 12px; border-bottom: 2px solid #ddd;">ID</th>
                <th style="padding: 12px; border-bottom: 2px solid #ddd;">Имя</th>
                <th style="padding: 12px; border-bottom: 2px solid #ddd;">Последняя проверка</th>
                <th style="padding: 12px; border-bottom: 2px solid #ddd;">Код ответа</th>
            </tr>
            </thead>
            <tbody>

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