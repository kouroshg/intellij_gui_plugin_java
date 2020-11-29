package com.speacode.uploader;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.Nullable;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.UIUtil;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.filechooser.*;

public class UploaderGUI extends DialogWrapper {

    private JPanel panelMain  = new JPanel(new GridBagLayout());
    private JTextField txtPath = new JTextField();
    private JButton btnBrowse = new JButton();
    private JTextField txtTitle = new JTextField();


    protected UploaderGUI(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        init();
        setTitle("Speacode Video Uploader");
        setOKButtonText("Upload");
        setSize(300,200);
        setResizable(false);
        initValidation();
    }

    @Override
    protected void doOKAction() {
        //upload file here
        super.doOKAction();
        TimerTask task = new TimerTask() {
            public void run() {
                showBalloon("Video Uploader", "Video upload has been completed", NotificationType.INFORMATION);
            }
        };

        Timer t = new Timer();
        t.schedule(task,500);
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if(txtPath.getText().isBlank())
            return new ValidationInfo("File path is required", txtPath);
        if(txtTitle.getText().isBlank())
            return new ValidationInfo("Title is required", txtTitle);
        return null;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        GridBag gridBag = new GridBag()
                .setDefaultInsets(new Insets(0,0,AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panelMain.setPreferredSize(new Dimension(300,200));
        panelMain.add(label("Title"), gridBag.nextLine().next().weightx(0.2));
        panelMain.add(txtTitle, gridBag.nextLine().next().weightx(0.8));
        panelMain.add(label("Video Path"), gridBag.nextLine().next().weightx(0.2));
        panelMain.add(txtPath, gridBag.nextLine().next().weightx(1.0));
        btnBrowse.addActionListener(new BrowseFileButtonClickListener());
        btnBrowse.setText("BROWSE");
        panelMain.add(btnBrowse,gridBag.nextLine().next().weightx(0.1));

        return panelMain;
    }

    private class BrowseFileButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            txtPath.setText(fileBrowser());
            fileBrowserNative((file)->{txtPath.setText(file.getPath());});
        }
    }

    private String fileBrowser()
    {
        JFrame dialogFrame = new JFrame();
        dialogFrame.setVisible(true);
        dialogFrame.setResizable(true);

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Video Formats: mp4, avi, mov, wmv", "mp4", "avi", "mov","wmv");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnVal = fileChooser.showOpenDialog(dialogFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return "";
    }

    private JComponent label(String text) {
        JBLabel label = new JBLabel(text);
        label.setComponentStyle( UIUtil.ComponentStyle.SMALL);
        return label;
    }

    private void showBalloon(String title, String content, NotificationType type)
    {
        NotificationGroup ng = new NotificationGroup("com.speacode.uploader", NotificationDisplayType.STICKY_BALLOON, true);
        Notification notification = new Notification(ng.getDisplayId(),title,content,type);
        notification.notify(null);
    }

    private void fileBrowserNative(Consumer<VirtualFile> callback)
    {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(
                true,
                false,
                false,
                false,
                false,
                false
        );

//        fileChooserDescriptor.withFileFilter(f-> extensionFilter(f));
        fileChooserDescriptor.setTitle( "Select the video you wish to upload");
        fileChooserDescriptor.setDescription("Your selected file will be uploaded");
        FileChooser.chooseFile(fileChooserDescriptor, null, null, callback);
    }

    private boolean extensionFilter(VirtualFile f) {
        return f.getExtension() == "mp4";
    }
}
