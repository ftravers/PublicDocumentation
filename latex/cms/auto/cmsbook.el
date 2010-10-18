(TeX-add-style-hook "cmsbook"
 (lambda ()
    (TeX-run-style-hooks
     "graphicx"
     "latex2e"
     "rep12"
     "report"
     "final"
     "letterpaper"
     "12pt"
     "ContentManagementOverview"
     "WebContentManagementOverview"
     "CmsToVirtualMachinesSegue"
     "VirtualMachineOverview"
     "ArchitectureV1")))

