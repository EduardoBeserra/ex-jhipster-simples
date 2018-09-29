import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IContinente } from 'app/shared/model/continente.model';
import { ContinenteService } from './continente.service';

@Component({
    selector: 'jhi-continente-update',
    templateUrl: './continente-update.component.html'
})
export class ContinenteUpdateComponent implements OnInit {
    private _continente: IContinente;
    isSaving: boolean;

    constructor(private continenteService: ContinenteService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ continente }) => {
            this.continente = continente;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.continente.id !== undefined) {
            this.subscribeToSaveResponse(this.continenteService.update(this.continente));
        } else {
            this.subscribeToSaveResponse(this.continenteService.create(this.continente));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IContinente>>) {
        result.subscribe((res: HttpResponse<IContinente>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get continente() {
        return this._continente;
    }

    set continente(continente: IContinente) {
        this._continente = continente;
    }
}
