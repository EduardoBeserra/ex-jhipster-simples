import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaComumSharedModule } from 'app/shared';
import {
    ContinenteComponent,
    ContinenteDetailComponent,
    ContinenteUpdateComponent,
    ContinenteDeletePopupComponent,
    ContinenteDeleteDialogComponent,
    continenteRoute,
    continentePopupRoute
} from './';

const ENTITY_STATES = [...continenteRoute, ...continentePopupRoute];

@NgModule({
    imports: [LojaComumSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ContinenteComponent,
        ContinenteDetailComponent,
        ContinenteUpdateComponent,
        ContinenteDeleteDialogComponent,
        ContinenteDeletePopupComponent
    ],
    entryComponents: [ContinenteComponent, ContinenteUpdateComponent, ContinenteDeleteDialogComponent, ContinenteDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaComumContinenteModule {}
