import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISubmissions } from 'app/shared/model/submissions.model';

@Component({
    selector: 'jhi-submissions-detail',
    templateUrl: './submissions-detail.component.html'
})
export class SubmissionsDetailComponent implements OnInit {
    submissions: ISubmissions;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ submissions }) => {
            this.submissions = submissions;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
