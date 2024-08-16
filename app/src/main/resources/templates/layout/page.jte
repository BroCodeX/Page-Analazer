@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body class="bg-light text-dark">

<header class="bg-dark py-3 mb-4 shadow-sm">
    <div class="container d-flex justify-content-between align-items-center">
        <h1 class="text-white fs-2 m-0">Анализатор страниц</h1>
        <nav class="d-flex gap-3">
            <a href="${NamedRoutes.rootPath()}" class="btn btn-secondary">
                Главная
            </a>
            <a href="${NamedRoutes.urlsPath()}" class="btn btn-secondary">
                Сайты
            </a>
        </nav>
    </div>
</header>
<div class="container-fluid py-4">
    @if(page != null && page.getFlash()!= null)
        <div class="alert alert-${page.getFlashType()}" role="alert">
            ${page.getFlash()}
        </div>
    @endif
</div>
${content}
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-OERcA2EU6Kv59MYOkLhTOFQ5k5E23hFzV2KR/IJ5RsOB1NYzj9K5G7niu735Sk7l" crossorigin="anonymous"></script>
</body>
</html>
