import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IContinente } from 'app/shared/model/continente.model';
import { Principal } from 'app/core';
import { ContinenteService } from './continente.service';

@Component({
    selector: 'jhi-continente',
    templateUrl: './continente.component.html'
})
export class ContinenteComponent implements OnInit, OnDestroy {
    continentes: IContinente[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private continenteService: ContinenteService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.continenteService.query().subscribe(
            (res: HttpResponse<IContinente[]>) => {
                this.continentes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInContinentes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IContinente) {
        return item.id;
    }

    registerChangeInContinentes() {
        this.eventSubscriber = this.eventManager.subscribe('continenteListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
