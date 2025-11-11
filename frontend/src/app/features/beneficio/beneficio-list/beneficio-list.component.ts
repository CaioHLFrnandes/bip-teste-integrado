import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { BeneficioService } from '../../../core/services/beneficio.service';
import { Beneficio } from '../../../core/models/beneficio.model';
import { MatDialog } from '@angular/material/dialog';
import { TransferDialogComponent } from '../transfer-dialog/transfer-dialog.component';
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";

@Component({
    selector: 'app-beneficio-list',
    templateUrl: './beneficio-list.component.html'
})
export class BeneficioListComponent implements OnInit,AfterViewInit  {
    beneficios: MatTableDataSource<Beneficio> = new MatTableDataSource();
    showTransfer = false;
    selectedFrom?: number;
    error?: string;
    displayedColumns: string[] = ['titular', 'saldo', 'acoes'];
    @ViewChild(MatSort) sort!: MatSort;
    constructor(private svc: BeneficioService,private dialog: MatDialog) {}
    ngAfterViewInit() {
        this.beneficios.sort = this.sort;
    }

    ngOnInit(): void { this.refresh(); }


    refresh() {
        this.svc.list().subscribe(res => {
            this.beneficios.data = res;
            this.beneficios.sort = this.sort; // habilita ordenação
        });
    }

    openTransfer(fromId: Beneficio) {
        const dialogRef = this.dialog.open(TransferDialogComponent, {
            data: { fromId },
            width: '600px',
            height: '500px'
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.refresh(); // atualizar a tabela
            }
        });
    }
    onClose(refresh:boolean) { this.showTransfer = false; if (refresh) this.refresh(); }
}