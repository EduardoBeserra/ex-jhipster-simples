import { NgModule } from '@angular/core';

import { LojaComumSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [LojaComumSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [LojaComumSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class LojaComumSharedCommonModule {}
