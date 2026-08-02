$ErrorActionPreference = 'Stop'

Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$OutputDir = Join-Path $PSScriptRoot 'Documentos_Comerciais'
New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

function Escape-Xml([string]$Text) {
    if ($null -eq $Text) { return '' }
    return [System.Security.SecurityElement]::Escape($Text)
}

function New-Run {
    param(
        [string]$Text,
        [switch]$Bold,
        [switch]$Italic,
        [string]$Color = '243247',
        [int]$Size = 22
    )
    $props = "<w:color w:val=`"$Color`"/><w:sz w:val=`"$Size`"/><w:szCs w:val=`"$Size`"/>"
    if ($Bold) { $props += '<w:b/>' }
    if ($Italic) { $props += '<w:i/>' }
    return "<w:r><w:rPr>$props</w:rPr><w:t xml:space=`"preserve`">$(Escape-Xml $Text)</w:t></w:r>"
}

function New-Paragraph {
    param(
        [string]$Text = '',
        [string]$Style = 'Normal',
        [string]$Align = 'left',
        [switch]$Bold,
        [switch]$Italic,
        [string]$Color = '243247',
        [int]$Size = 22,
        [int]$Before = 0,
        [int]$After = 100,
        [int]$Line = 300,
        [switch]$KeepNext,
        [switch]$PageBreakBefore
    )
    $pProps = "<w:pStyle w:val=`"$Style`"/><w:jc w:val=`"$Align`"/><w:spacing w:before=`"$Before`" w:after=`"$After`" w:line=`"$Line`" w:lineRule=`"auto`"/>"
    if ($KeepNext) { $pProps += '<w:keepNext/>' }
    if ($PageBreakBefore) { $pProps += '<w:pageBreakBefore/>' }
    $run = New-Run -Text $Text -Bold:$Bold -Italic:$Italic -Color $Color -Size $Size
    return "<w:p><w:pPr>$pProps</w:pPr>$run</w:p>"
}

function New-MixedParagraph {
    param([array]$Runs, [string]$Align = 'left', [int]$After = 100, [int]$Line = 300)
    $content = ''
    foreach ($r in $Runs) {
        $content += New-Run -Text $r.Text -Bold:([bool]$r.Bold) -Italic:([bool]$r.Italic) -Color $(if ($r.Color) { $r.Color } else { '243247' }) -Size $(if ($r.Size) { $r.Size } else { 22 })
    }
    return "<w:p><w:pPr><w:jc w:val=`"$Align`"/><w:spacing w:after=`"$After`" w:line=`"$Line`" w:lineRule=`"auto`"/></w:pPr>$content</w:p>"
}

function New-Bullet([string]$Text, [int]$Size = 22, [string]$Color = '243247') {
    return New-MixedParagraph -Runs @(
        @{ Text = '•  '; Bold = $true; Color = '16A6A1'; Size = $Size },
        @{ Text = $Text; Color = $Color; Size = $Size }
    ) -After 80 -Line 285
}

function New-PageBreak { return '<w:p><w:r><w:br w:type="page"/></w:r></w:p>' }

function New-Table {
    param(
        [array]$Rows,
        [array]$Widths,
        [switch]$Header,
        [string]$HeaderColor = '173F5F',
        [int]$FontSize = 19
    )
    $grid = ($Widths | ForEach-Object { "<w:gridCol w:w=`"$_`"/>" }) -join ''
    $xml = "<w:tbl><w:tblPr><w:tblW w:w=`"0`" w:type=`"auto`"/><w:tblBorders><w:top w:val=`"single`" w:sz=`"6`" w:color=`"D9E2EA`"/><w:left w:val=`"single`" w:sz=`"6`" w:color=`"D9E2EA`"/><w:bottom w:val=`"single`" w:sz=`"6`" w:color=`"D9E2EA`"/><w:right w:val=`"single`" w:sz=`"6`" w:color=`"D9E2EA`"/><w:insideH w:val=`"single`" w:sz=`"4`" w:color=`"D9E2EA`"/><w:insideV w:val=`"single`" w:sz=`"4`" w:color=`"D9E2EA`"/></w:tblBorders><w:tblCellMar><w:top w:w=`"100`" w:type=`"dxa`"/><w:left w:w=`"120`" w:type=`"dxa`"/><w:bottom w:w=`"100`" w:type=`"dxa`"/><w:right w:w=`"120`" w:type=`"dxa`"/></w:tblCellMar></w:tblPr><w:tblGrid>$grid</w:tblGrid>"
    for ($i = 0; $i -lt $Rows.Count; $i++) {
        $xml += '<w:tr>'
        for ($j = 0; $j -lt $Rows[$i].Count; $j++) {
            $shade = if ($Header -and $i -eq 0) { $HeaderColor } elseif ($i % 2 -eq 0) { 'F3F7FA' } else { 'FFFFFF' }
            $textColor = if ($Header -and $i -eq 0) { 'FFFFFF' } else { '243247' }
            $isBold = ($Header -and $i -eq 0)
            $cellText = [string]$Rows[$i][$j]
            $xml += "<w:tc><w:tcPr><w:tcW w:w=`"$($Widths[$j])`" w:type=`"dxa`"/><w:shd w:fill=`"$shade`"/><w:vAlign w:val=`"center`"/></w:tcPr>"
            $xml += New-Paragraph -Text $cellText -Bold:$isBold -Color $textColor -Size $FontSize -After 20 -Line 260
            $xml += '</w:tc>'
        }
        $xml += '</w:tr>'
    }
    return $xml + '</w:tbl>' + (New-Paragraph -Text '' -After 40)
}

function New-Callout {
    param([string]$Title, [string]$Text, [string]$Color = '16A6A1')
    $inner = (New-Paragraph -Text $Title -Bold -Color $Color -Size 24 -After 50) +
             (New-Paragraph -Text $Text -Color '243247' -Size 21 -After 20 -Line 285)
    return "<w:tbl><w:tblPr><w:tblW w:w=`"100%`" w:type=`"pct`"/><w:tblBorders><w:left w:val=`"single`" w:sz=`"22`" w:color=`"$Color`"/><w:top w:val=`"nil`"/><w:right w:val=`"nil`"/><w:bottom w:val=`"nil`"/></w:tblBorders></w:tblPr><w:tblGrid><w:gridCol w:w=`"9000`"/></w:tblGrid><w:tr><w:tc><w:tcPr><w:shd w:fill=`"F3F7FA`"/><w:tcMar><w:top w:w=`"180`" w:type=`"dxa`"/><w:left w:w=`"240`" w:type=`"dxa`"/><w:bottom w:w=`"180`" w:type=`"dxa`"/><w:right w:w=`"240`" w:type=`"dxa`"/></w:tcMar></w:tcPr>$inner</w:tc></w:tr></w:tbl>" + (New-Paragraph -Text '' -After 80)
}

function Get-StylesXml {
    return @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:styles xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:docDefaults><w:rPrDefault><w:rPr><w:rFonts w:ascii="Aptos" w:hAnsi="Aptos" w:eastAsia="Aptos"/><w:color w:val="243247"/><w:sz w:val="22"/></w:rPr></w:rPrDefault><w:pPrDefault><w:pPr><w:spacing w:after="100" w:line="300" w:lineRule="auto"/></w:pPr></w:pPrDefault></w:docDefaults>
  <w:style w:type="paragraph" w:default="1" w:styleId="Normal"><w:name w:val="Normal"/><w:qFormat/><w:rPr><w:rFonts w:ascii="Aptos" w:hAnsi="Aptos"/><w:sz w:val="22"/></w:rPr></w:style>
  <w:style w:type="paragraph" w:styleId="Title"><w:name w:val="Title"/><w:basedOn w:val="Normal"/><w:next w:val="Normal"/><w:qFormat/><w:rPr><w:rFonts w:ascii="Aptos Display" w:hAnsi="Aptos Display"/><w:b/><w:color w:val="173F5F"/><w:sz w:val="52"/></w:rPr></w:style>
  <w:style w:type="paragraph" w:styleId="Subtitle"><w:name w:val="Subtitle"/><w:basedOn w:val="Normal"/><w:next w:val="Normal"/><w:qFormat/><w:rPr><w:color w:val="16A6A1"/><w:sz w:val="28"/></w:rPr></w:style>
  <w:style w:type="paragraph" w:styleId="Heading1"><w:name w:val="heading 1"/><w:basedOn w:val="Normal"/><w:next w:val="Normal"/><w:qFormat/><w:pPr><w:keepNext/><w:spacing w:before="280" w:after="100"/></w:pPr><w:rPr><w:rFonts w:ascii="Aptos Display" w:hAnsi="Aptos Display"/><w:b/><w:color w:val="173F5F"/><w:sz w:val="32"/></w:rPr></w:style>
  <w:style w:type="paragraph" w:styleId="Heading2"><w:name w:val="heading 2"/><w:basedOn w:val="Normal"/><w:next w:val="Normal"/><w:qFormat/><w:pPr><w:keepNext/><w:spacing w:before="180" w:after="70"/></w:pPr><w:rPr><w:b/><w:color w:val="16A6A1"/><w:sz w:val="25"/></w:rPr></w:style>
</w:styles>
'@
}

function Write-Docx {
    param(
        [string]$Path,
        [string]$BodyXml,
        [string]$Title,
        [switch]$Landscape
    )
    $temp = Join-Path ([System.IO.Path]::GetTempPath()) ('docx_' + [guid]::NewGuid().ToString('N'))
    New-Item -ItemType Directory -Path $temp, (Join-Path $temp '_rels'), (Join-Path $temp 'word'), (Join-Path $temp 'word\_rels'), (Join-Path $temp 'docProps') | Out-Null

    $contentTypes = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
 <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
 <Default Extension="xml" ContentType="application/xml"/>
 <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
 <Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
 <Override PartName="/word/settings.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml"/>
 <Override PartName="/word/footer1.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml"/>
 <Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>
 <Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>
</Types>
'@
    $rootRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
 <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
 <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
</Relationships>
'@
    $docRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
 <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
 <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer" Target="footer1.xml"/>
</Relationships>
'@
    $settings = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:settings xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"><w:zoom w:percent="100"/><w:defaultTabStop w:val="720"/><w:compat/></w:settings>'
    $footer = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:ftr xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"><w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:rPr><w:color w:val="7B8794"/><w:sz w:val="18"/></w:rPr><w:t>JM-InvSM  |  Confidencial  |  </w:t></w:r><w:fldSimple w:instr="PAGE"><w:r><w:rPr><w:color w:val="7B8794"/><w:sz w:val="18"/></w:rPr><w:t>1</w:t></w:r></w:fldSimple></w:p></w:ftr>'
    $page = if ($Landscape) { '<w:pgSz w:w="15840" w:h="12240" w:orient="landscape"/><w:pgMar w:top="720" w:right="900" w:bottom="720" w:left="900" w:header="400" w:footer="400" w:gutter="0"/>' } else { '<w:pgSz w:w="11906" w:h="16838"/><w:pgMar w:top="1080" w:right="1080" w:bottom="1080" w:left="1080" w:header="500" w:footer="500" w:gutter="0"/>' }
    $document = "<?xml version=`"1.0`" encoding=`"UTF-8`" standalone=`"yes`"?><w:document xmlns:w=`"http://schemas.openxmlformats.org/wordprocessingml/2006/main`" xmlns:r=`"http://schemas.openxmlformats.org/officeDocument/2006/relationships`"><w:body>$BodyXml<w:sectPr>$page<w:footerReference w:type=`"default`" r:id=`"rId3`"/></w:sectPr></w:body></w:document>"
    $now = (Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ')
    $core = "<?xml version=`"1.0`" encoding=`"UTF-8`" standalone=`"yes`"?><cp:coreProperties xmlns:cp=`"http://schemas.openxmlformats.org/package/2006/metadata/core-properties`" xmlns:dc=`"http://purl.org/dc/elements/1.1/`" xmlns:dcterms=`"http://purl.org/dc/terms/`" xmlns:xsi=`"http://www.w3.org/2001/XMLSchema-instance`"><dc:title>$(Escape-Xml $Title)</dc:title><dc:creator>JM-InvSM</dc:creator><dc:subject>Documento comercial</dc:subject><dcterms:created xsi:type=`"dcterms:W3CDTF`">$now</dcterms:created><dcterms:modified xsi:type=`"dcterms:W3CDTF`">$now</dcterms:modified></cp:coreProperties>"
    $app = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes"><Application>Microsoft Office Word</Application><Company>JM-InvSM</Company></Properties>'

    $utf8 = New-Object System.Text.UTF8Encoding($false)
    $files = @{
        '[Content_Types].xml' = $contentTypes; '_rels\.rels' = $rootRels;
        'word\document.xml' = $document; 'word\styles.xml' = Get-StylesXml;
        'word\settings.xml' = $settings; 'word\footer1.xml' = $footer;
        'word\_rels\document.xml.rels' = $docRels; 'docProps\core.xml' = $core; 'docProps\app.xml' = $app
    }
    foreach ($item in $files.GetEnumerator()) {
        [System.IO.File]::WriteAllText((Join-Path $temp $item.Key), [string]$item.Value, $utf8)
    }
    if (Test-Path $Path) { Remove-Item -LiteralPath $Path -Force }
    $fileStream = [System.IO.File]::Open($Path, [System.IO.FileMode]::CreateNew)
    $archive = New-Object System.IO.Compression.ZipArchive($fileStream, [System.IO.Compression.ZipArchiveMode]::Create)
    foreach ($sourceFile in Get-ChildItem -LiteralPath $temp -File -Recurse) {
        $entryName = $sourceFile.FullName.Substring($temp.Length + 1).Replace('\', '/')
        [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($archive, $sourceFile.FullName, $entryName, [System.IO.Compression.CompressionLevel]::Optimal) | Out-Null
    }
    $archive.Dispose()
    $fileStream.Dispose()
    Remove-Item -LiteralPath $temp -Recurse -Force
}

# -----------------------------------------------------------------------------
# PROPOSTA COMERCIAL
# -----------------------------------------------------------------------------
$p = ''
$p += New-Paragraph -Text 'JM-InvSM' -Style 'Title' -Align 'center' -Bold -Color '173F5F' -Size 62 -Before 900 -After 120
$p += New-Paragraph -Text 'JM — Invoice & Stock Master' -Style 'Subtitle' -Align 'center' -Color '16A6A1' -Size 30 -After 500
$p += New-Paragraph -Text 'PROPOSTA COMERCIAL' -Align 'center' -Bold -Color '243247' -Size 38 -After 160
$p += New-Paragraph -Text 'Licenciamento, instalação e implementação de sistema de facturação e gestão de stock' -Align 'center' -Color '506274' -Size 24 -After 600
$p += New-Callout -Title 'CLIENTE' -Text '[NOME DO CLIENTE / EMPRESA]  |  NUIT: [__________]  |  Contacto: [__________]'
$p += New-Table -Rows @(
    @('Referência', 'PC-JM-2026-001'),
    @('Data de emissão', '13 de Julho de 2026'),
    @('Validade', '15 dias'),
    @('Proponente', '[NOME DA EMPRESA / PROPONENTE]'),
    @('Contacto', '[TELEFONE]  |  [EMAIL]')
) -Widths @(3100, 5600) -Header:$false -FontSize 20
$p += New-Paragraph -Text 'Documento confidencial preparado exclusivamente para o destinatário acima identificado.' -Align 'center' -Italic -Color '7B8794' -Size 18 -Before 300
$p += New-PageBreak

$p += New-Paragraph -Text 'Texto sugerido para o email' -Style 'Heading1' -Bold -Color '173F5F' -Size 34
$p += New-Callout -Title 'Assunto' -Text 'Proposta comercial — Sistema JM-InvSM para facturação e gestão de stock'
$p += New-Paragraph -Text 'Exmo.(a) Senhor(a) [NOME],' -Size 22
$p += New-Paragraph -Text 'Na sequência do nosso contacto, temos o prazer de enviar em anexo a proposta comercial para o licenciamento e implementação do JM-InvSM — JM Invoice & Stock Master.' -Size 22
$p += New-Paragraph -Text 'A solução foi concebida para apoiar pequenas e médias empresas na facturação, controlo de stock, vendas, compras, caixa, fornecedores e gestão operacional, mantendo a capacidade de funcionamento em ambiente local, mesmo sem ligação permanente à Internet.' -Size 22
$p += New-Paragraph -Text 'A proposta inclui a licença perpétua, instalação, configuração inicial e formação dos utilizadores. Permanecemos disponíveis para uma demonstração e para esclarecer qualquer questão.' -Size 22
$p += New-Paragraph -Text 'Com os melhores cumprimentos,' -Size 22 -Before 180
$p += New-Paragraph -Text '[NOME DO RESPONSÁVEL]' -Bold -Color '173F5F' -Size 22 -After 20
$p += New-Paragraph -Text '[CARGO / EMPRESA]  |  [TELEFONE]  |  [EMAIL]' -Color '506274' -Size 20
$p += New-PageBreak

$p += New-Paragraph -Text '1. Resumo executivo' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Paragraph -Text 'Apresentamos o JM-InvSM, uma solução desktop de facturação e gestão comercial orientada para pequenas e médias empresas. O sistema centraliza operações de vendas, stock, clientes, fornecedores, compras e controlo financeiro, reduzindo tarefas manuais e melhorando a rastreabilidade da informação.' -Size 22
$p += New-Callout -Title 'Objectivo da proposta' -Text 'Disponibilizar ao Cliente uma licença de utilização do JM-InvSM, efectuar a instalação e configuração inicial, preparar a base de dados e capacitar os utilizadores para o arranque operacional.'

$p += New-Paragraph -Text '2. Solução proposta' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Table -Rows @(
    @('Módulo', 'Capacidades principais'),
    @('Vendas e POS', 'Registo de vendas, pesquisa de produtos e clientes, descontos e pagamentos.'),
    @('Facturação', 'Emissão e consulta de documentos comerciais, facturas e recibos em função da versão instalada.'),
    @('Stock e inventário', 'Entradas, saídas, quantidades disponíveis, lotes, reservas, picking e movimentos.'),
    @('Armazéns', 'Gestão de produtos, preços e existências por armazém.'),
    @('Compras', 'Requisições, ordens de compra, recepção de mercadoria e facturas de fornecedores.'),
    @('Financeiro', 'Caixa, conta corrente, contas a pagar, pagamentos e controlo de dívidas.'),
    @('Cadastros e acesso', 'Clientes, fornecedores, empresas, utilizadores, perfis e permissões.')
) -Widths @(2600, 6200) -Header -FontSize 19

$p += New-Paragraph -Text '3. Escopo incluído' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
foreach ($item in @(
    'Uma licença perpétua para uma empresa, um estabelecimento e uma base de dados;',
    'Instalação do sistema num servidor/computador principal e configuração de até três postos na mesma rede local;',
    'Criação e configuração inicial da base de dados MySQL;',
    'Registo dos dados da empresa, impostos, armazém principal e utilizadores iniciais;',
    'Apoio à importação ou introdução do cadastro inicial, mediante ficheiro previamente acordado;',
    'Até seis horas de formação para os utilizadores-chave;',
    'Acompanhamento de arranque e garantia correctiva durante 30 dias;',
    'Manual rápido de operação e procedimento básico de cópias de segurança.'
)) { $p += New-Bullet $item }

$p += New-Paragraph -Text '4. Investimento' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Table -Rows @(
    @('Item', 'Quantidade', 'Valor'),
    @('Licença perpétua JM-InvSM', '1', '45.000 MT'),
    @('Instalação, configuração e formação', '1', '10.000 MT'),
    @('Investimento inicial', '', '55.000 MT'),
    @('Suporte e actualizações — opcional', 'Mensal', '2.500 MT'),
    @('Suporte e actualizações — opcional', 'Anual', '25.000 MT')
) -Widths @(4800, 1700, 2300) -Header -FontSize 20
$p += New-Callout -Title 'Condição comercial' -Text 'Os valores não incluem hardware, impressora, leitor de código de barras, licenças de sistema operativo, custos de rede ou IVA, quando legalmente aplicável.' -Color 'F0A202'

$p += New-Paragraph -Text 'Serviços e licenças adicionais' -Style 'Heading2' -Bold -Color '16A6A1' -Size 25
$p += New-Table -Rows @(
    @('Adicional', 'Valor de referência'),
    @('Posto adicional na mesma instalação', '8.500 MT'),
    @('Filial ou estabelecimento adicional', '17.500 MT'),
    @('Formação adicional', '1.500 MT/hora'),
    @('Desenvolvimento personalizado', 'Mediante orçamento')
) -Widths @(6000, 2800) -Header -FontSize 19

$p += New-PageBreak
$p += New-Paragraph -Text '5. Plano de implementação' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Paragraph -Text 'Prazo estimado: 7 a 10 dias úteis após adjudicação, pagamento inicial e disponibilização dos equipamentos, dados e responsáveis do Cliente.' -Size 22
$p += New-Table -Rows @(
    @('Fase', 'Actividade', 'Prazo estimado'),
    @('1', 'Levantamento e confirmação dos requisitos', '1 dia'),
    @('2', 'Instalação e configuração técnica', '1–2 dias'),
    @('3', 'Parametrização e carregamento inicial', '2–3 dias'),
    @('4', 'Formação e testes assistidos', '1–2 dias'),
    @('5', 'Entrada em produção e acompanhamento', '2 dias')
) -Widths @(900, 5700, 2200) -Header -FontSize 19

$p += New-Paragraph -Text '6. Responsabilidades das partes' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Paragraph -Text 'Do Proponente' -Style 'Heading2' -Bold -Color '16A6A1' -Size 25
foreach ($item in @('Instalar e configurar a versão contratada;', 'Executar testes básicos antes da entrega;', 'Realizar a formação prevista;', 'Corrigir falhas reproduzíveis abrangidas pela garantia;', 'Tratar como confidenciais os dados a que tiver acesso.')) { $p += New-Bullet $item }
$p += New-Paragraph -Text 'Do Cliente' -Style 'Heading2' -Bold -Color '16A6A1' -Size 25
foreach ($item in @('Disponibilizar equipamentos, rede local e acesso administrativo;', 'Fornecer dados correctos e validar saldos e cadastros iniciais;', 'Nomear um responsável para decisões e validações;', 'Executar cópias de segurança regulares após a entrega;', 'Usar o sistema de acordo com a legislação fiscal e contabilística aplicável.')) { $p += New-Bullet $item }

$p += New-Paragraph -Text '7. Garantia, suporte e manutenção' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Paragraph -Text 'A garantia correctiva de 30 dias cobre falhas reproduzíveis da versão entregue. Não cobre avarias de equipamento, vírus, alterações efectuadas por terceiros, perda de dados por ausência de cópias de segurança, mudanças legais posteriores nem novos desenvolvimentos.' -Size 22
$p += New-Paragraph -Text 'O plano opcional de suporte inclui assistência remota em horário comercial, correcções, actualizações disponibilizadas para a versão contratada e orientação operacional. Intervenções presenciais e deslocações são orçamentadas separadamente.' -Size 22

$p += New-Paragraph -Text '8. Condições de pagamento' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Table -Rows @(
    @('Momento', 'Percentagem', 'Valor'),
    @('Adjudicação e início dos trabalhos', '60%', '33.000 MT'),
    @('Instalação, formação e aceitação', '40%', '22.000 MT')
) -Widths @(4800, 1700, 2300) -Header -FontSize 20

$p += New-Paragraph -Text '9. Premissas e exclusões' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
foreach ($item in @(
    'Esta proposta não transfere o código-fonte nem os direitos de propriedade intelectual;',
    'A licença não pode ser revendida, copiada ou cedida a terceiros sem autorização escrita;',
    'Integrações com M-Pesa, e-Mola, bancos, contabilidade externa ou plataformas da Autoridade Tributária não estão incluídas, salvo indicação expressa;',
    'A emissão fiscal e a exportação de ficheiros para o e-Declaração dependem da versão validada e das orientações vigentes da Autoridade Tributária;',
    'Não se declara nesta proposta que o software possui certificação fiscal definitiva; qualquer processo de registo ou certificação será tratado separadamente;',
    'Alterações de escopo solicitadas após a adjudicação serão objecto de orçamento adicional.'
)) { $p += New-Bullet $item }

$p += New-PageBreak
$p += New-Paragraph -Text '10. Aceitação da proposta' -Style 'Heading1' -Bold -Color '173F5F' -Size 32
$p += New-Paragraph -Text 'Ao assinar este termo, o Cliente confirma que analisou e aceita o escopo, investimento, condições de pagamento, prazos, premissas e exclusões apresentados nesta proposta.' -Size 22
$p += New-Table -Rows @(
    @('PELO CLIENTE', 'PELO PROPONENTE'),
    @('Nome: ______________________________', 'Nome: ______________________________'),
    @('Cargo: ______________________________', 'Cargo: ______________________________'),
    @('Assinatura: __________________________', 'Assinatura: __________________________'),
    @('Data: ______ / ______ / __________', 'Data: ______ / ______ / __________'),
    @('Carimbo:', 'Carimbo:')
) -Widths @(4400, 4400) -Header -FontSize 19
$p += New-Callout -Title 'Próximo passo' -Text 'Após a aceitação e confirmação do pagamento inicial, será agendada a reunião de arranque e apresentada a lista de dados e equipamentos necessários.'

$proposalPath = Join-Path $OutputDir 'Proposta_Comercial_JM-InvSM.docx'
Write-Docx -Path $proposalPath -BodyXml $p -Title 'Proposta Comercial — JM-InvSM'

# -----------------------------------------------------------------------------
# APRESENTAÇÃO EXECUTIVA (DOCX EM PAISAGEM)
# -----------------------------------------------------------------------------
$s = ''
function Add-SlideTitle([string]$Number, [string]$Title, [string]$Subtitle = '') {
    $xml = New-Paragraph -Text $Number -Bold -Color '16A6A1' -Size 22 -Before 120 -After 80
    $xml += New-Paragraph -Text $Title -Bold -Color '173F5F' -Size 48 -After 100 -Line 520
    if ($Subtitle) { $xml += New-Paragraph -Text $Subtitle -Color '506274' -Size 25 -After 260 }
    return $xml
}

$s += New-Paragraph -Text 'JM-InvSM' -Align 'center' -Bold -Color '173F5F' -Size 72 -Before 1600 -After 150
$s += New-Paragraph -Text 'JM — Invoice & Stock Master' -Align 'center' -Color '16A6A1' -Size 34 -After 500
$s += New-Paragraph -Text 'APRESENTAÇÃO EXECUTIVA E PLANO DE ARRANQUE' -Align 'center' -Bold -Color '243247' -Size 32 -After 500
$s += New-Paragraph -Text 'Preparado para [NOME DO CLIENTE]' -Align 'center' -Color '506274' -Size 24 -After 40
$s += New-Paragraph -Text '13 de Julho de 2026  |  Versão 1.0' -Align 'center' -Color '7B8794' -Size 19
$s += New-PageBreak

$s += Add-SlideTitle '01' 'O desafio operacional' 'Quando a informação está dispersa, a empresa perde tempo, controlo e capacidade de decisão.'
foreach ($item in @(
    'Vendas, stock, compras e pagamentos registados em locais diferentes;',
    'Dificuldade em conhecer a quantidade realmente disponível;',
    'Erros manuais na facturação, preços e controlo de dívidas;',
    'Pouca visibilidade sobre caixa, clientes e fornecedores;',
    'Dependência de folhas de cálculo e processos informais.'
)) { $s += New-Bullet -Text $item -Size 27 }
$s += New-Callout -Title 'Resultado pretendido' -Text 'Uma única fonte de informação para apoiar a operação diária e a gestão do negócio.'
$s += New-PageBreak

$s += Add-SlideTitle '02' 'A solução JM-InvSM' 'Uma plataforma local que integra os principais processos comerciais.'
$s += New-Table -Rows @(
    @('VENDER', 'CONTROLAR', 'COMPRAR', 'DECIDIR'),
    @('POS e facturação', 'Stock e armazéns', 'Fornecedores e recepções', 'Caixa e indicadores'),
    @('Clientes e pagamentos', 'Lotes e movimentos', 'Ordens de compra', 'Contas e dívidas')
) -Widths @(3200, 3200, 3200, 3200) -Header -FontSize 22
$s += New-Callout -Title 'Concebido para PMEs' -Text 'Operação simples, base de dados relacional e funcionamento sem dependência permanente da Internet.'
$s += New-PageBreak

$s += Add-SlideTitle '03' 'Fluxo integrado da operação' 'Cada movimento alimenta automaticamente a etapa seguinte.'
$s += New-Table -Rows @(
    @('1. COMPRA', '2. RECEPÇÃO', '3. STOCK', '4. VENDA', '5. FINANCEIRO'),
    @('Requisição e ordem', 'Conferência da mercadoria', 'Entrada por armazém/lote', 'POS, factura e recibo', 'Caixa, contas e pagamentos')
) -Widths @(2500, 2500, 2500, 2500, 2500) -Header -FontSize 21
$s += New-Paragraph -Text 'COMPRA  →  RECEPÇÃO  →  DISPONIBILIDADE  →  VENDA  →  PAGAMENTO  →  ANÁLISE' -Align 'center' -Bold -Color '16A6A1' -Size 28 -Before 340 -After 260
foreach ($item in @('Redução da duplicação de registos;', 'Maior rastreabilidade dos movimentos;', 'Informação actualizada para decisão operacional.')) { $s += New-Bullet -Text $item -Size 26 }
$s += New-PageBreak

$s += Add-SlideTitle '04' 'Principais módulos' 'Cobertura funcional progressiva, de acordo com a versão contratada.'
$s += New-Table -Rows @(
    @('Área', 'Funcionalidades'),
    @('Comercial', 'POS, facturas, recibos, clientes, descontos e pagamentos'),
    @('Inventário', 'Produtos, categorias, preços, stock, lotes, reservas e picking'),
    @('Logística', 'Armazéns, movimentos, transferências e stock em trânsito'),
    @('Compras', 'Requisições, ordens, recepções e facturas de fornecedor'),
    @('Financeiro', 'Caixa, conta corrente, contas a pagar e pagamentos'),
    @('Administração', 'Empresa, impostos, utilizadores, perfis e permissões')
) -Widths @(3200, 9300) -Header -FontSize 22
$s += New-PageBreak

$s += Add-SlideTitle '05' 'Benefícios para o Cliente' 'Tecnologia traduzida em melhorias práticas no dia-a-dia.'
$s += New-Table -Rows @(
    @('BENEFÍCIO', 'IMPACTO ESPERADO'),
    @('Controlo de stock', 'Menos rupturas, diferenças e vendas sem disponibilidade'),
    @('Rapidez no atendimento', 'Operações de balcão mais consistentes'),
    @('Rastreabilidade', 'Histórico de compras, vendas, movimentos e pagamentos'),
    @('Controlo financeiro', 'Melhor acompanhamento de caixa, dívidas e fornecedores'),
    @('Operação offline', 'Continuidade mesmo sem Internet permanente'),
    @('Base para crescimento', 'Estrutura preparada para novos módulos e relatórios')
) -Widths @(4000, 8500) -Header -FontSize 22
$s += New-PageBreak

$s += Add-SlideTitle '06' 'Segurança e continuidade' 'A disponibilidade do sistema depende de boas práticas técnicas e operacionais.'
foreach ($item in @(
    'Utilizadores e permissões para limitar o acesso por função;',
    'Base de dados central para consistência da informação;',
    'Registo estruturado de operações e movimentos;',
    'Cópias de segurança periódicas, com responsabilidade definida;',
    'Funcionamento em rede local sem dependência permanente da Internet;',
    'Actualizações e apoio técnico através do plano de suporte.'
)) { $s += New-Bullet -Text $item -Size 26 }
$s += New-Callout -Title 'Regra essencial' -Text 'A empresa deve manter cópias de segurança diárias e testar regularmente a recuperação dos dados.' -Color 'F0A202'
$s += New-PageBreak

$s += Add-SlideTitle '07' 'Plano de implementação' 'Arranque estimado em 7 a 10 dias úteis, sujeito à disponibilidade dos dados e equipamentos.'
$s += New-Table -Rows @(
    @('FASE', 'ENTREGA', 'RESPONSÁVEL'),
    @('1. Levantamento', 'Requisitos, equipamentos e dados confirmados', 'Cliente + Proponente'),
    @('2. Instalação', 'Aplicação, base de dados e rede configuradas', 'Proponente'),
    @('3. Parametrização', 'Empresa, impostos, utilizadores e cadastros', 'Cliente + Proponente'),
    @('4. Formação', 'Utilizadores-chave capacitados', 'Proponente'),
    @('5. Produção', 'Operação assistida e validação', 'Cliente + Proponente')
) -Widths @(2700, 6700, 3100) -Header -FontSize 21
$s += New-PageBreak

$s += Add-SlideTitle '08' 'Formação e adopção' 'O sucesso depende tanto da tecnologia como da disciplina de utilização.'
$s += New-Table -Rows @(
    @('SESSÃO', 'PARTICIPANTES', 'CONTEÚDO'),
    @('Administração', 'Gestor / administrador', 'Configurações, utilizadores, segurança e backup'),
    @('Vendas', 'Operadores de caixa', 'POS, clientes, documentos, pagamentos e fecho'),
    @('Stock e compras', 'Fiel de armazém / comprador', 'Produtos, entradas, lotes, movimentos e recepções'),
    @('Gestão', 'Direcção / supervisão', 'Consultas, controlo e validações operacionais')
) -Widths @(2700, 3600, 6200) -Header -FontSize 21
$s += New-Callout -Title 'Incluído' -Text 'Até seis horas de formação e acompanhamento inicial durante o período de garantia.'
$s += New-PageBreak

$s += Add-SlideTitle '09' 'Suporte e evolução' 'Um modelo simples para manter o sistema operacional e actualizado.'
$s += New-Table -Rows @(
    @('PERÍODO / SERVIÇO', 'COBERTURA'),
    @('Garantia — 30 dias', 'Correcção de falhas reproduzíveis da versão entregue'),
    @('Suporte mensal ou anual', 'Assistência remota, correcções e actualizações disponibilizadas'),
    @('Serviços adicionais', 'Formação extra, deslocações, integrações e desenvolvimento personalizado'),
    @('Evolução fiscal', 'Adequações tratadas de acordo com requisitos oficiais e escopo contratado')
) -Widths @(4100, 8400) -Header -FontSize 22
$s += New-Paragraph -Text 'Canal de suporte: [TELEFONE / WHATSAPP / EMAIL]' -Align 'center' -Bold -Color '16A6A1' -Size 26 -Before 300
$s += New-PageBreak

$s += Add-SlideTitle '10' 'Próximos passos' 'Da aceitação da proposta ao início da operação.'
foreach ($item in @(
    '1. Assinatura da proposta comercial;',
    '2. Pagamento de 60% do investimento inicial;',
    '3. Reunião de arranque e entrega da lista de pré-requisitos;',
    '4. Instalação, configuração e carregamento inicial;',
    '5. Formação, testes assistidos e entrada em produção;',
    '6. Aceitação final e início do período de garantia.'
)) { $s += New-Bullet -Text $item -Size 27 }
$s += New-Callout -Title 'Contacto' -Text '[NOME DO RESPONSÁVEL]  |  [TELEFONE]  |  [EMAIL]'
$s += New-Paragraph -Text 'Obrigado.' -Align 'center' -Bold -Color '173F5F' -Size 40 -Before 250

$presentationPath = Join-Path $OutputDir 'Apresentacao_Executiva_JM-InvSM.docx'
Write-Docx -Path $presentationPath -BodyXml $s -Title 'Apresentação Executiva — JM-InvSM' -Landscape

Write-Output $proposalPath
Write-Output $presentationPath



