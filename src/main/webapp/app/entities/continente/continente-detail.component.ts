import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContinente } from 'app/shared/model/continente.model';

@Component({
    selector: 'jhi-continente-detail',
    templateUrl: './continente-detail.component.html'
})
export class ContinenteDetailComponent implements OnInit {
    continente: IContinente;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ continente }) => {
            this.continente = continente;
        });
    }

    previousState() {
        window.history.back();
    }
}
