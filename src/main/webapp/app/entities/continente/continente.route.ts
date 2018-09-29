import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Continente } from 'app/shared/model/continente.model';
import { ContinenteService } from './continente.service';
import { ContinenteComponent } from './continente.component';
import { ContinenteDetailComponent } from './continente-detail.component';
import { ContinenteUpdateComponent } from './continente-update.component';
import { ContinenteDeletePopupComponent } from './continente-delete-dialog.component';
import { IContinente } from 'app/shared/model/continente.model';

@Injectable({ providedIn: 'root' })
export class ContinenteResolve implements Resolve<IContinente> {
    constructor(private service: ContinenteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((continente: HttpResponse<Continente>) => continente.body));
        }
        return of(new Continente());
    }
}

export const continenteRoute: Routes = [
    {
        path: 'continente',
        component: ContinenteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Continentes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'continente/:id/view',
        component: ContinenteDetailComponent,
        resolve: {
            continente: ContinenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Continentes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'continente/new',
        component: ContinenteUpdateComponent,
        resolve: {
            continente: ContinenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Continentes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'continente/:id/edit',
        component: ContinenteUpdateComponent,
        resolve: {
            continente: ContinenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Continentes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const continentePopupRoute: Routes = [
    {
        path: 'continente/:id/delete',
        component: ContinenteDeletePopupComponent,
        resolve: {
            continente: ContinenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Continentes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
