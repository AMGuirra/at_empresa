package atualizacao.crud.controle;

import atualizacao.crud.modelo.Atualizacao;
import atualizacao.crud.repositorio.AtualizacaoRepositorio;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportExcelController {

    @Autowired
    private AtualizacaoRepositorio atualizacaoRepositorio;

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        // Obtém os dados ordenados por ID
        List<Atualizacao> atualizacoes = atualizacaoRepositorio.findAllByOrderByIdAsc();

        // Cria o arquivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Atualizações");

        // Formatter para datas no padrão brasileiro
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Configura o cabeçalho
        String[] columns = {
            "ID", 
            "Cliente", 
            "Usuário", 
            "Observação", 
            "Quantidade PC", 
            "Status", 
            "Data Pedido", 
            "Data Início", 
            "Data Fim"
        };

        // Estilo do cabeçalho
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Estilo para células de data
        CellStyle dateCellStyle = createDateCellStyle(workbook);

        // Preenche as linhas com os dados
        int rowNum = 1;
        for (Atualizacao atualizacao : atualizacoes) {
            Row row = sheet.createRow(rowNum++);
            
            // ID
            row.createCell(0).setCellValue(atualizacao.getId());

            // Cliente
            row.createCell(1).setCellValue(
                atualizacao.getCliente() != null ? atualizacao.getCliente().getNome() : "N/A"
            );

            // Usuário
            row.createCell(2).setCellValue(
                atualizacao.getUsuario() != null ? atualizacao.getUsuario().getNome() : "N/A"
            );

            // Demais campos
            row.createCell(3).setCellValue(atualizacao.getObs());
            row.createCell(4).setCellValue(atualizacao.getQtPc());
            row.createCell(5).setCellValue(
                atualizacao.getStatus() != null ? atualizacao.getStatus().getNome() : "N/A"
            );

            // Datas formatadas
            formatDateCell(row, 6, atualizacao.getDtPedido(), dateFormatter, dateCellStyle);
            formatDateCell(row, 7, atualizacao.getDtInicio(), dateFormatter, dateCellStyle);
            formatDateCell(row, 8, atualizacao.getDtFim(), dateFormatter, dateCellStyle);
        }

        // Ajusta o tamanho das colunas
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Configura o download do arquivo
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=atualizacoes.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void formatDateCell(Row row, int cellIndex, java.time.LocalDate date, 
                              DateTimeFormatter formatter, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        if (date != null) {
            cell.setCellValue(date.format(formatter));
            cell.setCellStyle(style);
        } else {
            cell.setCellValue("N/A");
        }
    }
}