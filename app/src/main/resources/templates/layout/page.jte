@import hexlet.code.util.NamedRoutes
@import gg.jte.Content
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Анализатор страниц</title>
</head>
<body style="background-color: #f3f4f6; font-family: Arial, sans-serif; color: #333; margin: 0; padding: 0;">

<header style="background-color: #3a3f44; padding: 20px 40px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); display: flex; align-items: center; justify-content: space-between;">
    <h1 style="font-size: 28px; margin: 0; color: #ffffff; letter-spacing: 1px; text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);">Анализатор страниц</h1>
    <nav style="display: flex; gap: 20px;">
        <a href="${NamedRoutes.rootPath()}" style="text-decoration: none; color: #ddd; font-size: 18px; padding: 10px 20px; background-color: #55595e; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); transition: background-color 0.3s, transform 0.2s;">
            Главная
        </a>
        <a href="${NamedRoutes.urlsPath()}" style="text-decoration: none; color: #ddd; font-size: 18px; padding: 10px 20px; background-color: #55595e; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); transition: background-color 0.3s, transform 0.2s;">
            Сайты
        </a>
    </nav>
</header>

<div class="mx-auto p-4 py-md-5">
    ${content}
</div>

</body>
</html>




