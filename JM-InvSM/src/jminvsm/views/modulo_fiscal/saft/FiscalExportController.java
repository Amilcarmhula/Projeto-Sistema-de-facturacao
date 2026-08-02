package jminvsm.views.modulo_fiscal.saft;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import jminvsm.util.SessaoAplicacao;
import jminvsm.fiscal.model.FiscalExportOutcome;
import jminvsm.fiscal.model.FiscalValidationResult;
import jminvsm.fiscal.service.FiscalExportService;

public class FiscalExportController implements Initializable {

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private TextField txtDestino;
    @FXML private Label labEmpresa;
    @FXML private Label labEstado;
    @FXML private TextArea txtResultado;

    private final FiscalExportService service = new FiscalExportService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate today = LocalDate.now();
        dpInicio.setValue(today.withDayOfMonth(1));
        dpFim.setValue(today.withDayOfMonth(today.lengthOfMonth()));
        txtDestino.setText(Paths.get(System.getProperty("user.home"), "JM-InvSM", "ExportacoesFiscais").toString());
        if (SessaoAplicacao.getEmpresaData() != null) {
            labEmpresa.setText(SessaoAplicacao.getEmpresaData().getRazao_emp() + " â€” NUIT "
                    + SessaoAplicacao.getEmpresaData().getNuit_emp());
        } else {
            labEmpresa.setText("Empresa nÃ£o seleccionada");
        }
        labEstado.setText("Modelo JM-AT-BASE-1.0 â€” provisÃ³rio, nÃ£o oficial");
    }

    @FXML
    private void escolherDestino() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Pasta para exportaÃ§Ãµes fiscais");
        File current = new File(txtDestino.getText());
        if (current.isDirectory()) chooser.setInitialDirectory(current);
        File selected = chooser.showDialog(txtDestino.getScene().getWindow());
        if (selected != null) txtDestino.setText(selected.getAbsolutePath());
    }

    @FXML
    private void validar() {
        try {
            FiscalValidationResult result = service.validate(companyId(), start(), end());
            showValidation(result);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    @FXML
    private void exportarFase1() {
        try {
            FiscalExportOutcome outcome = service.exportPhase1(
                    companyId(), start(), end(), destination());
            showOutcome("Fase 1 â€” Excel/CSV", outcome);
        } catch (FiscalExportService.FiscalValidationException ex) {
            showValidation(ex.getValidation());
            alert(Alert.AlertType.ERROR, "ExportaÃ§Ã£o bloqueada", ex.getMessage());
        } catch (Exception ex) {
            showError(ex);
        }
    }

    @FXML
    private void exportarFase2() {
        try {
            FiscalExportOutcome outcome = service.exportPhase2(
                    companyId(), start(), end(), destination());
            showOutcome("Fase 2 â€” SAF-T XML provisÃ³rio", outcome);
        } catch (FiscalExportService.FiscalValidationException ex) {
            showValidation(ex.getValidation());
            alert(Alert.AlertType.ERROR, "ExportaÃ§Ã£o bloqueada", ex.getMessage());
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showOutcome(String phase, FiscalExportOutcome outcome) {
        String size = String.format("%.2f MB", outcome.getPackageSize() / 1024.0 / 1024.0);
        String warning = outcome.getPackageSize() > FiscalExportService.RECOMMENDED_MAX_BYTES
                ? "\nAVISO: pacote superior a 2 MB; reduza o perÃ­odo antes da submissÃ£o."
                : "";
        txtResultado.setText(phase + " concluÃ­da.\n"
                + "Documentos: " + outcome.getDocumentCount() + "\n"
                + "Avisos: " + outcome.getValidation().getWarningCount() + "\n"
                + "Tamanho: " + size + "\n"
                + "SHA-256: " + outcome.getSha256() + "\n"
                + "Ficheiro: " + outcome.getPackagePath() + warning);
        labEstado.setText("ExportaÃ§Ã£o concluÃ­da");
        alert(Alert.AlertType.INFORMATION, "ExportaÃ§Ã£o fiscal", "Ficheiro criado com sucesso.");
    }

    private void showValidation(FiscalValidationResult result) {
        StringBuilder text = new StringBuilder();
        text.append("Erros: ").append(result.getErrorCount())
                .append(" | Avisos: ").append(result.getWarningCount()).append('\n');
        for (FiscalValidationResult.Issue issue : result.getIssues()) {
            text.append(issue.getSeverity()).append(" | ")
                    .append(issue.getDocumentNumber()).append(" | ")
                    .append(issue.getField()).append(" | ")
                    .append(issue.getMessage()).append('\n');
        }
        txtResultado.setText(text.toString());
        labEstado.setText(result.isValid() ? "ValidaÃ§Ã£o concluÃ­da sem erros" : "Existem erros bloqueantes");
    }

    private int companyId() {
        if (SessaoAplicacao.getEmpresaData() == null || SessaoAplicacao.getEmpresaData().getId() <= 0) {
            throw new IllegalStateException("Seleccione/entre numa empresa antes de exportar.");
        }
        return SessaoAplicacao.getEmpresaData().getId();
    }

    private LocalDate start() {
        if (dpInicio.getValue() == null) throw new IllegalArgumentException("Informe a data inicial.");
        return dpInicio.getValue();
    }

    private LocalDate end() {
        if (dpFim.getValue() == null) throw new IllegalArgumentException("Informe a data final.");
        if (dpFim.getValue().isBefore(start())) throw new IllegalArgumentException("PerÃ­odo invÃ¡lido.");
        return dpFim.getValue();
    }

    private Path destination() {
        if (txtDestino.getText() == null || txtDestino.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Seleccione a pasta de destino.");
        }
        return Paths.get(txtDestino.getText().trim());
    }

    private void showError(Exception ex) {
        labEstado.setText("Falha na operaÃ§Ã£o");
        txtResultado.setText(ex.getClass().getSimpleName() + ": " + ex.getMessage()
                + "\n\nConfirme se o script 07_FISCAL_SAFT_MOZ.sql foi aplicado.");
        alert(Alert.AlertType.ERROR, "Erro fiscal", ex.getMessage());
    }

    private void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message == null ? "Erro nÃ£o especificado." : message);
        alert.showAndWait();
    }
}


