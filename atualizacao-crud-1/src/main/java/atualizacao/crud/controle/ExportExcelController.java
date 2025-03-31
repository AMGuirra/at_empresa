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
        CellStyle headerStyle = getHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Preenche as linhas com os dados
        int rowNum = 1;
        for (Atualizacao atualizacao : atualizacoes) {
            Row row = sheet.createRow(rowNum++);
            
            // ID
            row.createCell(0).setCellValue(atualizacao.getId());

            // Cliente (com verificação de null)
            row.createCell(1).setCellValue(
                atualizacao.getCliente() != null ? atualizacao.getCliente().getNome() : "N/A"
            );

            // Usuário (com verificação de null)
            row.createCell(2).setCellValue(
                atualizacao.getUsuario() != null ? atualizacao.getUsuario().getNome() : "N/A"
            );

            // Demais campos
            row.createCell(3).setCellValue(atualizacao.getObs());
            row.createCell(4).setCellValue(atualizacao.getQtPc());
            row.createCell(5).setCellValue(
                atualizacao.getStatus() != null ? atualizacao.getStatus().getNome() : "N/A"
            );

            // Datas (formatadas como String)
            row.createCell(6).setCellValue(
                atualizacao.getDtPedido() != null ? atualizacao.getDtPedido().toString() : "N/A"
            );
            row.createCell(7).setCellValue(
                atualizacao.getDtInicio() != null ? atualizacao.getDtInicio().toString() : "N/A"
            );
            row.createCell(8).setCellValue(
                atualizacao.getDtFim() != null ? atualizacao.getDtFim().toString() : "N/A"
            );
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

    // Estilo profissional para o cabeçalho
    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}