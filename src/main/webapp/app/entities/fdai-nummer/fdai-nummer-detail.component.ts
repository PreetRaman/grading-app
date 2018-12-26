import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFdaiNummer } from 'app/shared/model/fdai-nummer.model';

@Component({
    selector: 'jhi-fdai-nummer-detail',
    templateUrl: './fdai-nummer-detail.component.html'
})
export class FdaiNummerDetailComponent implements OnInit {
    fdaiNummer: IFdaiNummer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fdaiNummer }) => {
            this.fdaiNummer = fdaiNummer;
        });
    }

    previousState() {
        window.history.back();
    }
}
