import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LojaComumProdutoModule } from './produto/produto.module';
import { LojaComumContinenteModule } from './continente/continente.module';
import { LojaComumCidadeModule } from './cidade/cidade.module';
import { LojaComumEstadoModule } from './estado/estado.module';
import { LojaComumParametroModule } from './parametro/parametro.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LojaComumProdutoModule,
        LojaComumContinenteModule,
        LojaComumCidadeModule,
        LojaComumEstadoModule,
        LojaComumParametroModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaComumEntityModule {}
