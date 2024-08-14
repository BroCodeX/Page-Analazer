@template.layout.page(
content = @`
    <main style="max-width: 800px; margin: 50px auto; padding: 60px 40px; background-color: white; border-radius: 10px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); text-align: center;">
        <h2 style="font-size: 36px; margin-bottom: 30px; color: #3a3f44;">Анализатор страниц</h2>
        <p style="font-size: 18px; margin-bottom: 30px; color: #555;">Бесплатно проверяйте сайты на SEO пригодность</p>
        <form>
            <input type="url" placeholder="Введите ссылку" name="url" style="padding: 18px; width: calc(100% - 40px); max-width: 600px; border: 2px solid #ddd; border-radius: 8px; margin-bottom: 20px; font-size: 16px; box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1); transition: border-color 0.3s;">
            <p style="font-size: 14px; color: #666; margin-top: 0;">Пример: https://www.example.com</p>
            <button type="submit" style="background-color: #34a853; color: white; padding: 15px 50px; border: none; border-radius: 5px; font-size: 18px; cursor: pointer; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); transition: transform 0.2s, box-shadow 0.2s;">
                Проверить
            </button>
        </form>
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

        const button = document.querySelector('button[type="submit"]');
        button.addEventListener('mouseover', () => {
            button.style.boxShadow = '0 4px 10px rgba(0, 0, 0, 0.3)';
            button.style.transform = 'translateY(-2px)';
        });
        button.addEventListener('mouseout', () => {
            button.style.boxShadow = '0 2px 5px rgba(0, 0, 0, 0.2)';
            button.style.transform = 'translateY(0)';
        });

        const input = document.querySelector('input[type="url"]');
        input.addEventListener('focus', () => {
            input.style.borderColor = '#34a853';
        });
        input.addEventListener('blur', () => {
            input.style.borderColor = '#ddd';
        });
    </script>
`
)