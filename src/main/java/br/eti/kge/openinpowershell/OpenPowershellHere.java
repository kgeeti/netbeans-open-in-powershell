package br.eti.kge.openinpowershell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.support.ProjectOperations;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "br.eti.kge.openpowershell.OpenPowershellHere"
)
@ActionRegistration(
        displayName = "#CTL_OpenPowershellHere"
)

@ActionReference(path = "Projects/Actions", position = 0)
@Messages("CTL_OpenPowershellHere=Open in Powershell")
public final class OpenPowershellHere implements ActionListener {

    private final Project project;

    public OpenPowershellHere(Project project) {
        this.project = project;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String projectName;

        if (project != null) {
            ProjectInformation info = project.getLookup().lookup(ProjectInformation.class);
            if (info != null) {
                projectName = info.getDisplayName();
            } else {
                projectName = "Desconhecido";
            }

            // Obtém o diretório do projeto
            FileObject projectDir = project.getProjectDirectory();

            // Converte para File para obter o caminho absoluto
            File projectFolder = FileUtil.toFile(projectDir);
            String absolutePath = projectFolder != null ? projectFolder.getAbsolutePath() : projectDir.getPath();

            /* Apenas para debug
            // Informações adicionais
            String projectDirName = projectDir.getName();
            String relativePath = projectDir.getPath();

            String message = String.format(
                    "Nome do Projeto: %s%n"
                    + "Nome da Pasta: %s%n"
                    + "Caminho Relativo: %s%n"
                    + "Caminho Absoluto: %s",
                    projectName,
                    projectDirName,
                    relativePath,
                    absolutePath
            );

            DialogDisplayer.getDefault().notify(
                    new NotifyDescriptor.Message(
                            message, NotifyDescriptor.INFORMATION_MESSAGE)
            );

             */
            try {
                new ProcessBuilder(
                        "powershell.exe",
                        "Start-Process powershell.exe '-NoExit \"[Console]::Title = ''NetBeans''\"' -WorkingDirectory '" + absolutePath + "' "
                ).start();

            } catch (IOException ex) {

                DialogDisplayer.getDefault().notify(
                        new NotifyDescriptor.Message(
                                "Ops... failed to open Powershell into " + projectName + " folder..\n" + ex.getMessage(), NotifyDescriptor.INFORMATION_MESSAGE)
                );
            }

        }

    }
}
