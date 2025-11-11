import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {BeneficioService} from "../../../core/services/beneficio.service";
import {Beneficio} from "../../../core/models/beneficio.model";


interface Beneficiario {
    id: number;
    titular: string;
}

@Component({
    selector: 'app-transfer-dialog',
    templateUrl: './transfer-dialog.component.html',
    styleUrls: ['./transfer-dialog.component.css']
})
export class TransferDialogComponent implements OnInit {
    toId!: number;
    amount!: number;
    error = '';
    loading = false;
    beneficiarios: Beneficio[] = [];
    fromIdName: string = '';

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: any,
        private beneficioService: BeneficioService,
        private dialogRef: MatDialogRef<TransferDialogComponent>
    ) {}

    ngOnInit(): void {
        this.fromIdName = this.data.fromIdName || `Nome : ${this.data.fromId.titular}`;
        this.loadBeneficiarios();
    }

    loadBeneficiarios() {
        this.beneficioService.list().subscribe({
            next: (res) => {
                this.beneficiarios = res.filter(b => b.id !== this.data.fromId.id);
                this.toId = null;
            },
            error: (err) => {
                this.error = 'Erro ao carregar beneficiários';
            }
        });
    }

    send() {
        this.loading = true;
        this.error = '';
        this.beneficioService
            .transfer(this.data.fromId.id, this.toId, this.amount)
            .subscribe({
                next: () => this.dialogRef.close(true),
                error: (err) => {
                    this.error = err?.error?.message ?? 'Erro ao transferir';
                    this.loading = false;
                }
            });
    }

    cancel() {
        this.dialogRef.close();
    }
}
